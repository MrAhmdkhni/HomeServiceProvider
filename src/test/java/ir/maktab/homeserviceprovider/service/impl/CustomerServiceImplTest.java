package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.offer.TimeType;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.service.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private ExpertService expertService;
    @Autowired
    private SubServiceService subServiceService;

    @Test
    void findByEmail() {
        Customer customer = new Customer("eee1", "eee1", "09999999922", "eee1@gmail.com", "aaAA1111");
        customerService.saveOrUpdate(customer);
        Optional<Customer> optionalCustomer = customerService.findByEmail(customer.getEmail());
        assertTrue(optionalCustomer.isPresent());
    }

    @Test
    void findByPhoneNumber() {
        Customer customer = new Customer("eee2", "eee2", "09999999933", "eee2@gmail.com", "aaAA1111");
        customerService.saveOrUpdate(customer);
        Optional<Customer> optionalCustomer = customerService.findByPhoneNumber(customer.getPhoneNumber());
        assertTrue(optionalCustomer.isPresent());
    }

    @Test
    void editPassword() {
        Customer customer = new Customer("eee3", "eee3", "09999999944", "eee3@gmail.com", "aaAA1111");
        customerService.saveOrUpdate(customer);
        int affectedRows = customerService.editPassword(customer.getId(), "2222MMmm");
        assertEquals(1, affectedRows);
    }

    @Test
    void existsByEmail() {
        Customer customer = new Customer("eee4", "eee4", "09999999955", "eee4@gmail.com", "aaAA1111");
        customerService.saveOrUpdate(customer);
        boolean isExist = customerService.existsByEmail(customer.getEmail());
        assertTrue(isExist);
    }

    @Test
    void signUp() {
        Customer customer = new Customer("eee5", "eee5", "09999999966", "eee5@gmail.com", "aaAA1111");
        customerService.signUp(customer);
        Optional<Customer> optionalCustomer = customerService.findById(customer.getId());
        assertEquals(customer.getEmail(), optionalCustomer.get().getEmail());
    }

    @Test
    void addOrder() {
        Customer customer = new Customer("eee6", "eee6", "09999999977", "eee6@gmail.com", "aaAA1111");
        customerService.saveOrUpdate(customer);
        SubService subService = new SubService("sub-11", 12000L, "---");
        subServiceService.saveOrUpdate(subService);
        Order order = new Order(15550L, "---", LocalDateTime.of(1401, 11, 15, 18, 30), "tehran");
        customerService.addOrder(customer.getId(), subService.getId(), order);
        Optional<Order> optionalOrder = orderService.findById(order.getId());
        assertEquals(order.getExecutionTime(), optionalOrder.get().getExecutionTime());
    }

    @Test
    void addCommentForExpertPerformance() {
        Expert expert = new Expert("nnnn", "nnnn", "09999999111", "nnnn@gmail.com", "11MMnn11");
        expert.setScore(0);
        expertService.saveOrUpdate(expert);
        Order order  = new Order(10L, "---", LocalDateTime.of(1401, 11, 15, 18, 30), "cccc");
        orderService.saveOrUpdate(order);
        Comment comment = new Comment(5, "excellent");
        customerService.addCommentForExpertPerformance(order.getId(), expert.getId(), comment);

        Optional<Comment> optionalComment = commentService.findById(comment.getId());
        assertEquals(comment.getComment(), optionalComment.get().getComment());
        Optional<Expert> optionalExpert = expertService.findById(expert.getId());
        assertEquals(comment.getScore(), optionalExpert.get().getScore());
    }

    @Test
    void viewCustomerOrders() {
        Customer customer = new Customer("eee7", "eee7", "09999999988", "eee7@gmail.com", "aaAA1111");
        customerService.saveOrUpdate(customer);
        Order order  = new Order(10L, "---", LocalDateTime.of(1401, 11, 15, 18, 30), "cccc");
        order.setCustomer(customer);
        orderService.saveOrUpdate(order);
        List<Order> orders = customerService.viewCustomerOrders(customer.getId());
        Optional<Order> optionalOrder = orderService.findById(order.getId());
        assertEquals(orders.get(0).getCreationDate(), optionalOrder.get().getCreationDate());
    }

    @Test
    void selectOffer() {
        Order order  = new Order(10L, "---", LocalDateTime.of(1401, 11, 15, 18, 30), "cccc");
        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT_SELECTION);
        orderService.saveOrUpdate(order);
        Offer offer = new Offer("---", 10L, TimeType.MINUTE, 30, LocalDateTime.of(1401, 11, 15, 18, 30));
        offer.setOrder(order);
        offer.setIsAccept(false);
        offerService.saveOrUpdate(offer);
        customerService.selectOffer(offer.getId());

        Optional<Offer> optionalOffer = offerService.findById(offer.getId());
        Optional<Order> optionalOrder = orderService.findById(order.getId());
        assertTrue(optionalOffer.get().getIsAccept());
        assertEquals(OrderStatus.WAITING_FOR_EXPERT_TO_COME, optionalOrder.get().getOrderStatus());
    }

    @Test
    void changeOrderStatusAfterExpertComes() {
        Order order  = new Order(10L, "---", LocalDateTime.of(1401, 11, 15, 18, 30), "cccc");
        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT_TO_COME);
        orderService.saveOrUpdate(order);
        int affectedRows = customerService.changeOrderStatusAfterExpertComes(order.getId());
        assertEquals(1, affectedRows);
    }

    @Test
    void changeOrderStatusAfterStarted() {
        Order order  = new Order(10L, "---", LocalDateTime.of(1401, 11, 15, 18, 30), "cccc");
        order.setOrderStatus(OrderStatus.STARTED);
        orderService.saveOrUpdate(order);
        int affectedRows = customerService.changeOrderStatusAfterStarted(order.getId());
        assertEquals(1, affectedRows);
    }

}