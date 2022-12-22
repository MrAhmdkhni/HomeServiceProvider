package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Test
    void findByCustomerIdWithOnePram() {
        List<Order> orders = orderService.findByCustomerId(1L);
        Optional<Order> optionalOrder = orderService.findById(9L);
        assertEquals(orders.get(0).getCreationDate(), optionalOrder.get().getCreationDate());
    }

    @Test
    void findByCustomerIdWithTwoPram() {
        List<Order> orders = orderService.findByCustomerId(1L, OrderStatus.WAITING_FOR_EXPERT_SELECTION);
        Optional<Order> optionalOrder = orderService.findById(9L);
        assertEquals(orders.get(0).getCreationDate(), optionalOrder.get().getCreationDate());
    }

    @Test
    void changeOrderStatus() {
        int affectedRows = orderService.changeOrderStatus(1L, OrderStatus.WAITING_FOR_EXPERT_SUGGESTION, OrderStatus.STARTED);
        assertEquals(1, affectedRows);
    }
}