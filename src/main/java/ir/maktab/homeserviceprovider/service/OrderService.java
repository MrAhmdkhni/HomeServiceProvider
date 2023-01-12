package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.dto.OrderFilterDTO;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;

import java.util.List;

public interface OrderService extends BaseService<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);

    int changeOrderStatus(Long orderId, OrderStatus orderStatus, OrderStatus newOrderStatus);

    List<Order> findBySubServiceIdAndOrderStatus(Long subServiceId, OrderStatus orderStatusOne, OrderStatus orderStatusTwo);

    List<Order> ordersFilter(OrderFilterDTO orderDTO);

    int calculateNumberOfRegisteredOrders(Long customerId);

    int calculateNumberOfRegisteredOrders(Long customerId, OrderStatus orderStatus);
}
