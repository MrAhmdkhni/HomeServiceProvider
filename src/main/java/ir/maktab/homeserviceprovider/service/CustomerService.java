package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;

import java.util.List;
import java.util.Optional;

public interface CustomerService extends BaseService<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    int editPassword(Long customerId, String newPassword);

    boolean existsByEmail(String email);

    void signUp(Customer customer);

    List<MainService> findAllMainService();

    List<SubService> findAllSubService();

    void addOrder(Long customerId, Long subServiceId , Order order);

    void addCommentForExpertPerformance(Long orderId, Comment comment);

    List<Order> viewCustomerOrders(Long customerId);

    List<Offer> viewOrderOffers(Long orderId);

    void selectOfferForOrder(Long offerId, Long orderId);

    int changeOrderStatusAfterExpertComes(Long orderId);

    int changeOrderStatusAfterStarted(Long orderId);
}
