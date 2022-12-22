package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.repository.OrderRepository;
import ir.maktab.homeserviceprovider.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl
        extends BaseServiceImpl<Order, Long, OrderRepository> implements OrderService {

    public OrderServiceImpl(OrderRepository repository) {
        super(repository);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        try {
            return repository.findByCustomerId(customerId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Order> findByCustomerId(Long customerId, OrderStatus orderStatus) {
        try {
            return repository.findByCustomerId(customerId, orderStatus);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public int changeOrderStatus(Long orderId, OrderStatus orderStatus, OrderStatus newOrderStatus) {
        return repository.changeOrderStatus(orderId, orderStatus, newOrderStatus);
    }
}
