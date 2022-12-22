package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.entity.person.Admin;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.exception.*;
import ir.maktab.homeserviceprovider.repository.AdminRepository;
import ir.maktab.homeserviceprovider.service.AdminService;
import ir.maktab.homeserviceprovider.service.ExpertService;
import ir.maktab.homeserviceprovider.service.MainServiceService;
import ir.maktab.homeserviceprovider.service.SubServiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl
        extends BaseServiceImpl<Admin, Long, AdminRepository> implements AdminService {

    private final MainServiceService mainServiceService;
    private final SubServiceService subServiceService;
    private final ExpertService expertService;

    public AdminServiceImpl(AdminRepository repository, MainServiceService mainServiceService, SubServiceService subServiceService, ExpertService expertService) {
        super(repository);
        this.mainServiceService = mainServiceService;
        this.subServiceService = subServiceService;
        this.expertService = expertService;
    }


    @Override
    public void addMainService(MainService mainService) {
        if (mainServiceService.findByName(mainService.getName()).isPresent()) {
            throw new MainServiceAlreadyExistException("this main service already exist!");
        }
        mainServiceService.saveOrUpdate(mainService);
    }

    @Override
    public int deleteMainService(MainService mainService) {
        int affectedRows = mainServiceService.deleteByName(mainService.getName());
        if (affectedRows == 0)
            throw new MainServiceNotFoundException("this main service dose not exist!");
        return affectedRows;
    }

    @Override
    public void addSubService(Long mainServiceId, SubService subService) {
        Optional<MainService> mainService = mainServiceService.findById(mainServiceId);
        if (mainService.isEmpty()) {
            throw new MainServiceNotFoundException("this main service dose not exist!");
        } else if (subServiceService.findByName(subService.getName()).isPresent()) {
            throw new SubServiceAlreadyExistException("this sub-service already exist!");
        }
        SubService newSubService = new SubService(subService.getName(), subService.getBasePrice(), subService.getDescription(), mainService.get());
        subServiceService.saveOrUpdate(newSubService);
    }

    @Override
    public int deleteSubService(SubService subService) {
        int affectedRows = subServiceService.deleteByName(subService.getName());
        if(affectedRows == 0) {
            throw new SubServiceNotFoundException("this sub-service dose not exist!");
        }
        return affectedRows;
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
            throw new SubServiceNotFoundException("there is not no sub-services");
        return subServices;
    }

    @Override
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
    public int changeExpertStatus(Long expertId, ExpertStatus expertStatus) {
        int affectedRows = expertService.editExpertStatus(expertId, expertStatus);
        if (affectedRows == 0)
            throw new ExpertNotFoundException("this expert does not exist!");
        return affectedRows;
    }
}
