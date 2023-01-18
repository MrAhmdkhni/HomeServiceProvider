package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.dto.CustomerFilterDTO;
import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;

import java.util.List;
import java.util.Optional;

public interface CustomerService extends BaseService<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByUsername(String username);

    int editPassword(Long customerId, String newPassword);

    boolean existsByEmail(String email);

    void signUp(Customer customer);

    void signUpWithValidation(Customer customer);

    void confirmCustomerAccount(String confirmationToken);

    List<MainService> findAllMainService();

    List<SubService> findAllSubService();

    void addOrder(Long customerId, Long subServiceId , Order order);

    void addCommentForExpertPerformance(Long orderId, Long expertId, Comment comment);

    List<Offer> viewOrderOffersBaseOnProposedPrice(Long orderId);

    List<Offer> viewOrderOffersBaseOnExpertScore(Long orderId);

    void selectOffer(Long offerId);

    int changeOrderStatusAfterExpertComes(Long orderId);

    int changeOrderStatusAfterStarted(Long orderId);

    void updateCredit(Long customerId, Long newCredit);

    void payFromCredit(Long orderId, Long customerId, Long expertId, Long amount);

    List<Customer> customersFilter(CustomerFilterDTO customerDTO);

    List<Order> viewOrderHistory(Long customerId);

    List<Order> viewOrderHistory(Long customerId, OrderStatus orderStatus);

    Long viewCredit(Long customerId);
}
