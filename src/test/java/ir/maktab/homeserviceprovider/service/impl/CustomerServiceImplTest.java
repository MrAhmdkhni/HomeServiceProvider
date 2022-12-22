package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private OfferService offerService;

    @Test
    void findByEmail() {
        Optional<Customer> optionalCustomer = customerService.findByEmail("abcdd@gmail.com");
        assertFalse(optionalCustomer.isPresent());
    }

    @Test
    void findByPhoneNumber() {
        Optional<Customer> optionalCustomer = customerService.findByPhoneNumber("09999999991");
        assertTrue(optionalCustomer.isPresent());
    }

    @Test
    void editPassword() {
        int affectedRows = customerService.editPassword(1L, "2222MMmm");
        assertEquals(1, affectedRows);
    }

    @Test
    void existsByEmail() {
        boolean isExist = customerService.existsByEmail("abcd@gmail.com");
        assertTrue(isExist);
    }

    @Test
    void signUp() {
        Customer customer = new Customer("mahdi", "kia","09999999995", "mahdi@gmail.com", "aaAA1111");
        customerService.signUp(customer);
        Optional<Customer> optionalCustomer = customerService.findById(customer.getId());
        assertEquals(customer.getEmail(), optionalCustomer.get().getEmail());
    }

    @Test
    void addOrder() {
        Order order = new Order(15550L, "---", LocalDateTime.of(1401, 11, 15, 18, 30), "tehran");
        customerService.addOrder(1L, 1L, order);
        Optional<Order> optionalOrder = orderService.findById(order.getId());
        assertEquals(order.getExecutionTime(), optionalOrder.get().getExecutionTime());
    }

    @Test
    void addCommentForExpertPerformance() {
        Comment comment = new Comment(5, "excellent");
        customerService.addCommentForExpertPerformance(1L, comment);
        Optional<Comment> optionalComment = commentService.findById(comment.getId());
        optionalComment.ifPresent(newComment -> {
            assertEquals(comment.getComment(), newComment.getComment());
        });
    }

    @Test
    void viewCustomerOrders() {
        List<Order> orders = customerService.viewCustomerOrders(1L);
        Optional<Order> order = orderService.findById(9L);
        assertEquals(orders.get(0).getCreationDate(), order.get().getCreationDate());
    }

    @Test
    void viewOrderOffers() {
        List<Offer> offers = customerService.viewOrderOffers(10L);
        Optional<Offer> offer = offerService.findById(3L);
        assertEquals(offers.get(0).getCreationDate(), offer.get().getCreationDate());
    }

    @Test
    void selectOfferForOrder() {
        customerService.selectOfferForOrder(3L, 10L);
        Optional<Offer> optionalOffer = offerService.findById(3L);
        Optional<Order> optionalOrder = orderService.findById(10L);
        assertTrue(optionalOffer.get().getIsAccept());
        assertEquals(OrderStatus.WAITING_FOR_EXPERT_TO_COME, optionalOrder.get().getOrderStatus());
    }

    @Test
    void changeOrderStatusAfterExpertComes() {
        int affectedRows = customerService.changeOrderStatusAfterExpertComes(10L);
        assertEquals(1, affectedRows);
    }

    @Test
    void changeOrderStatusAfterStarted() {
        int affectedRows = customerService.changeOrderStatusAfterStarted(10L);
        assertEquals(1, affectedRows);
    }

}