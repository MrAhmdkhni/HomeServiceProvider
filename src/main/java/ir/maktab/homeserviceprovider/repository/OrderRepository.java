package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findByCustomerId(Long customerId);

    List<Order> findByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus);

    @Modifying
    @Query("""
            update Order o
            set o.orderStatus = :newOrderStatus
            where o.id = :orderId and o.orderStatus = :orderStatus
            """)
    int changeOrderStatus(Long orderId, OrderStatus orderStatus, OrderStatus newOrderStatus);

    @Query("""
            from Order o
            where o.subService.id = :subServiceId
            and (o.orderStatus = :orderStatusOne or o.orderStatus = :orderStatusTwo)
            """)
    List<Order> findBySubServiceIdAndOrderStatus(Long subServiceId, OrderStatus orderStatusOne, OrderStatus orderStatusTwo);

    @Query("""
            select count(o.customer.id) from Order o
            where o.customer.id = :customerId
            """)
    int calculateNumberOfRegisteredOrders(Long customerId);

    @Query("""
            select count(o.customer.id) from Order o
            where o.customer.id = :customerId and o.orderStatus = :orderStatus
            """)
    int calculateNumberOfRegisteredOrders(Long customerId, OrderStatus orderStatus);
}
