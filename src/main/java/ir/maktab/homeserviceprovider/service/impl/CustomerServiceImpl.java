package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.exception.*;
import ir.maktab.homeserviceprovider.repository.CustomerRepository;
import ir.maktab.homeserviceprovider.service.*;
import ir.maktab.homeserviceprovider.util.Validation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl
        extends BaseServiceImpl<Customer, Long, CustomerRepository>
        implements CustomerService {

    private final MainServiceService mainServiceService;
    private final SubServiceService subServiceService;
    private final OrderService orderService;
    private final CommentService commentService;
    private final OfferService offerService;

    public CustomerServiceImpl(CustomerRepository repository, MainServiceService mainServiceService, SubServiceService subServiceService, OrderService orderService, CommentService commentService, OfferService offerService) {
        super(repository);
        this.mainServiceService = mainServiceService;
        this.subServiceService = subServiceService;
        this.orderService = orderService;
        this.commentService = commentService;
        this.offerService = offerService;
    }


    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            return repository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByPhoneNumber(String phoneNumber) {
        try {
            return repository.findByPhoneNumber(phoneNumber);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public int editPassword(Long customerId, String newPassword) {
        Validation.checkPassword(newPassword);
        int affectedRows = repository.editPassword(customerId, newPassword);
        if (affectedRows == 0)
            throw new PasswordException("the password could not edited!");
        return affectedRows;
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public void signUp(Customer customer) {
        if (findByPhoneNumber(customer.getPhoneNumber()).isPresent())
            throw new DuplicatePhoneNumberException("this phone number already exist!");
        else if (findByEmail(customer.getEmail()).isPresent())
            throw new DuplicateEmailException("this email number already exist!");

        Validation.checkPhoneNumber(customer.getPhoneNumber());
        Validation.checkEmail(customer.getEmail());
        Validation.checkPassword(customer.getPassword());
        customer.setCredit(0L);
        saveOrUpdate(customer);
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
    public void addOrder(Long customerId, Long subServiceId, Order order) {
        Optional<Customer> customer = findById(customerId);
        if (customer.isEmpty())
            throw new CustomerNotFoundException("there is no customers!");
        Optional<SubService> subService = subServiceService.findById(subServiceId);
        if (subService.isEmpty())
            throw new SubServiceNotFoundException("this sub-service dose not exist!");
        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT_SUGGESTION);
        order.setCustomer(customer.get());
        order.setSubService(subService.get());
        orderService.saveOrUpdate(order);
    }

    @Override
    public void addCommentForExpertPerformance(Long orderId, Comment comment) {
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("there is no orders!");
        }
        comment.setOrder(order.get());
        commentService.saveOrUpdate(comment);
    }

    @Override
    public List<Order> viewCustomerOrders(Long customerId) {
        List<Order> orders = orderService.findByCustomerId(customerId);
        if (orders.isEmpty())
            throw new OrderNotFoundException("there is no orders!");
        return orders;
    }

    @Override
    public List<Offer> viewOrderOffers(Long orderId) {
        List<Offer> offers = offerService.findByOrderId(orderId);
        if (offers.isEmpty())
            throw new OfferNotFoundException("there is no offers!");
        return offers;
    }

    @Override
    public void selectOfferForOrder(Long offerId, Long orderId) {
        int offerAffectedRows = offerService.editIsAccept(offerId, true);
        if (offerAffectedRows == 0)
            throw new OfferNotFoundException("there is no offers, or the offer accepted!");
        int orderAffectedRows = orderService.changeOrderStatus(orderId, OrderStatus.WAITING_FOR_EXPERT_SELECTION, OrderStatus.WAITING_FOR_EXPERT_TO_COME);
        if (orderAffectedRows == 0)
            throw new OrderNotFoundException("there is no orders, or the status of this order not \"WAITING_FOR_EXPERT_SELECTION\"!");
    }

    @Override
    public int changeOrderStatusAfterExpertComes(Long orderId) {
        int affectedRows = orderService.changeOrderStatus(orderId, OrderStatus.WAITING_FOR_EXPERT_TO_COME, OrderStatus.STARTED);
        if (affectedRows == 0)
            throw new OrderStatusException("the status of this order cannot be changed!");
        return affectedRows;
    }

    @Override
    public int changeOrderStatusAfterStarted(Long orderId) {
        int affectedRows = orderService.changeOrderStatus(orderId, OrderStatus.STARTED, OrderStatus.DONE);
        if (affectedRows == 0)
            throw new OrderStatusException("the status of this order cannot be changed!");
        return affectedRows;
    }

}
