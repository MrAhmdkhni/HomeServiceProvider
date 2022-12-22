package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.exception.*;
import ir.maktab.homeserviceprovider.repository.ExpertRepository;
import ir.maktab.homeserviceprovider.service.ExpertService;
import ir.maktab.homeserviceprovider.service.OfferService;
import ir.maktab.homeserviceprovider.service.OrderService;
import ir.maktab.homeserviceprovider.util.Validation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ExpertServiceImpl
        extends BaseServiceImpl<Expert, Long, ExpertRepository> implements ExpertService {

    private final OfferService offerService;
    private final OrderService orderService;

    public ExpertServiceImpl(ExpertRepository repository, OfferService offerService, OrderService orderService) {
        super(repository);
        this.offerService = offerService;
        this.orderService = orderService;
    }


    @Override
    public Optional<Expert> findByEmail(String email) {
        try {
            return repository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Expert> findByPhoneNumber(String phoneNumber) {
        try {
            return repository.findByPhoneNumber(phoneNumber);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public int editPassword(Long expertId, String newPassword) {
        Validation.checkPassword(newPassword);
        int affectedRows = repository.editPassword(expertId, newPassword);
        if (affectedRows == 0)
            throw new PasswordException("the password could not edited!");
        return affectedRows;
    }

    @Override
    @Transactional
    public int editExpertStatus(Long expertId, ExpertStatus expertStatus) {
        return repository.editExpertStatus(expertId, expertStatus);
    }

    @Override
    public void signUp(Expert expert, File image) {
        if (findByPhoneNumber(expert.getPhoneNumber()).isPresent())
            throw new DuplicatePhoneNumberException("this phone number already exist!");
        else if (findByEmail(expert.getEmail()).isPresent())
            throw new DuplicateEmailException("this email number already exist!");
        Validation.checkPhoneNumber(expert.getPhoneNumber());
        Validation.checkEmail(expert.getEmail());
        Validation.checkPassword(expert.getPassword());

        byte[] byteFile = new byte[(int) image.length()];
        String [] path = image.getPath().split("\\.");
        if (!path[path.length - 1].equalsIgnoreCase("JPG"))
            throw new ImageFormatException("the format of the image is incorrect!");
        try {
            if (Files.size(Paths.get(image.getPath())) > 300000)
                throw new ImageSizeException("the size of the image is bigger than 300 kilo byte!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        expert.setImage(byteFile);
        expert.setScore(0);
        expert.setCredit(0L);
        expert.setExpertStatus(ExpertStatus.NEW);
        saveOrUpdate(expert);
    }

    @Override
    public void addOfferForOrder(Long expertId, Long orderId, Offer offer) {
        Optional<Expert> expert = findById(expertId);
        if (expert.isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        if (!expert.get().getExpertStatus().equals(ExpertStatus.CONFIRMED))
            throw new ExpertNoAccessException("the status of expert is not \"confirmed\"");
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty())
            throw new OrderNotFoundException("there is no order!");

        Set<SubService> expertSubServices = expert.get().getSubServices();
        expertSubServices.forEach(subService -> {
            if (Objects.equals(subService.getId(), order.get().getSubService().getId())) {
                if (order.get().getSubService().getBasePrice() > offer.getProposedPrice())
                    throw new PriceException("the proposed-price should not be lower than the base-price!");
                if (!order.get().getOrderStatus().equals(OrderStatus.WAITING_FOR_EXPERT_SUGGESTION))
                    throw new OrderStatusException("the status of this order not \"WAITING_FOR_EXPERT_SUGGESTION\"!");

                offer.setIsAccept(false);
                offer.setExpert(expert.get());
                offer.setOrder(order.get());
                offerService.saveOrUpdate(offer);
                orderService.changeOrderStatus(orderId, OrderStatus.WAITING_FOR_EXPERT_SUGGESTION, OrderStatus.WAITING_FOR_EXPERT_SELECTION);
            }
        });
    }
}
