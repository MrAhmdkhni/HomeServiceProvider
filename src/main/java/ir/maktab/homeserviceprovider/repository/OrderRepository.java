package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("from Order o where o.customer.id = :customerId")
    List<Order> findByCustomerId(Long customerId);

    @Query("from Order o where o.customer.id = :customerId and o.orderStatus = :orderStatus")
    List<Order> findByCustomerId(Long customerId, OrderStatus orderStatus);

    @Modifying
    @Query("""
            update Order o
            set o.orderStatus = :newOrderStatus
            where o.id = :orderId and o.orderStatus = :orderStatus
            """)
    int changeOrderStatus(Long orderId, OrderStatus orderStatus, OrderStatus newOrderStatus);
}
