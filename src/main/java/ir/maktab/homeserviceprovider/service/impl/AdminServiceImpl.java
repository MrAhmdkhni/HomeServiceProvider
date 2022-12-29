package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.dto.CustomerFilterDTO;
import ir.maktab.homeserviceprovider.dto.ExpertFilterDTO;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.Admin;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.exception.*;
import ir.maktab.homeserviceprovider.repository.AdminRepository;
import ir.maktab.homeserviceprovider.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl
        extends BaseServiceImpl<Admin, Long, AdminRepository> implements AdminService {

    private final MainServiceService mainServiceService;
    private final SubServiceService subServiceService;
    private final ExpertService expertService;
    private final OfferService offerService;
    private final CustomerService customerService;

    public AdminServiceImpl(AdminRepository repository, MainServiceService mainServiceService, SubServiceService subServiceService, ExpertService expertService, OfferService offerService, CustomerService customerService) {
        super(repository);
        this.mainServiceService = mainServiceService;
        this.subServiceService = subServiceService;
        this.expertService = expertService;
        this.offerService = offerService;
        this.customerService = customerService;
    }


    @Override
    public void addMainService(MainService mainService) {
        if (mainServiceService.findByName(mainService.getName()).isPresent())
            throw new MainServiceAlreadyExistException("this main service already exist!");
        mainServiceService.saveOrUpdate(mainService);
    }

    @Override
    public int deleteMainService(Long mainServiceId) {
        if(mainServiceService.findById(mainServiceId).isEmpty())
            throw new MainServiceNotFoundException("this main service dose not exist!");
        return mainServiceService.deleteMainServiceById(mainServiceId);
    }

    @Override
    public void addSubService(Long mainServiceId, SubService subService) {
        Optional<MainService> mainService = mainServiceService.findById(mainServiceId);
        if (mainService.isEmpty())
            throw new MainServiceNotFoundException("this main service dose not exist!");
        if (subServiceService.findByName(subService.getName()).isPresent())
            throw new SubServiceAlreadyExistException("this sub-service already exist!");
        subServiceService.saveOrUpdate(subService);
    }

    @Override
    public int deleteSubService(Long subServiceId) {
        if (subServiceService.findById(subServiceId).isEmpty())
            throw new SubServiceNotFoundException("this sub-service dose not exist!");
        return subServiceService.deleteSubServiceById(subServiceId);
    }

    @Override
    public void addExpertToSubService(Long subServiceId, Long expertId) {
        Optional<SubService> subService = subServiceService.findById(subServiceId);
        if (subService.isEmpty()) {
            throw new SubServiceNotFoundException("this sub-service dose not exist!");
        }
        Optional<Expert> expert = expertService.findById(expertId);
        if (expert.isEmpty()) {
            throw new ExpertNotFoundException("this expert does not exist!");
        } else if (!expert.get().getExpertStatus().equals(ExpertStatus.CONFIRMED)) {
            throw new ExpertNoAccessException("the status of expert is not \"confirmed\"");
        }
        expert.get().addSubService(subService.get());
        expertService.saveOrUpdate(expert.get());
    }

    @Override
    public void deleteExpertFromSubService(Long subServiceId, Long expertId) {
        Optional<SubService> subService = subServiceService.findById(subServiceId);
        if (subService.isEmpty()) {
            throw new SubServiceNotFoundException("this sub-service dose not exist!");
        }
        Optional<Expert> expert = expertService.findById(expertId);
        if (expert.isEmpty()) {
            throw new ExpertNotFoundException("this expert does not exist!");
        }
        expert.get().deleteSubService(subService.get());
        expertService.saveOrUpdate(expert.get());
    }

    @Override
    public List<MainService> findAllMainService() {
        List<MainService> mainServices = mainServiceService.findAll();
        if (mainServices.isEmpty())
            throw new MainServiceNotFoundException("there is no main-services");
        return mainServices;
    }

    @Override
    public List<SubService> findAllSubService() {
        List<SubService> subServices = subServiceService.findAll();
        if (subServices.isEmpty())
            throw new SubServiceNotFoundException("there is no sub-services");
        return subServices;
    }

    @Override
    @Transactional
    public int editSubServiceBasePriceAndDescription(Long subServiceId, Long basePrice, String description) {
        int affectedRows = subServiceService.editBasePriceAndDescription(subServiceId, basePrice, description);
        if (affectedRows == 0)
            throw new SubServiceNotFoundException("this sub-service dose not exist!");
        return affectedRows;
    }

    @Override
    public List<Expert> findAllExpert() {
        List<Expert> experts = expertService.findAll();
        if (experts.isEmpty())
            throw new ExpertNotFoundException("there is no experts!");
        return experts;
    }

    @Override
    @Transactional
    public int changeExpertStatus(Long expertId, ExpertStatus expertStatus) {
        if (expertService.findById(expertId).isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        return expertService.changeExpertStatus(expertId, expertStatus);
    }

    @Override
    public boolean checkExpertDelayForDoingWork(Long offerId){
        Optional<Offer> offer = offerService.findById(offerId);
        if (offer.isEmpty())
            throw new OfferNotFoundException("there is no offers!");
        Order order = offer.get().getOrder();
        if (!order.getOrderStatus().equals(OrderStatus.PAID))
            throw new OrderStatusException("the status of this order is not yet \"PAID\"!");
        Optional<Expert> expert = expertService.findById(offer.get().getExpert().getId());
        if (expert.isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        if (order.getUpdateTime().compareTo(offer.get().getEndTime()) > 0) {
            long timeDifference = ChronoUnit.HOURS.between(offer.get().getEndTime(), order.getUpdateTime());
            expert.get().setScore(expert.get().getScore() - (int)timeDifference);
            expertService.saveOrUpdate(expert.get());
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public int changeExpertActivation(Long expertId, Boolean isActive) {
        Optional<Expert> expert = expertService.findById(expertId);
        if (expert.isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        if (expert.get().getScore() > 0 | expert.get().getIsActive().equals(false))
            throw new ExpertActivationException("the expert score is either positive or the expert account is inactive");
        return expertService.changeExpertActivation(expertId, isActive);
    }

    @Override
    public List<Expert> expertsFilter(ExpertFilterDTO expertFilterDTO) {
        return expertService.expertsFilter(expertFilterDTO);
    }

    @Override
    public List<Customer> customersFilter(CustomerFilterDTO customerFilterDTO) {
        return customerService.customersFilter(customerFilterDTO);
    }
}
