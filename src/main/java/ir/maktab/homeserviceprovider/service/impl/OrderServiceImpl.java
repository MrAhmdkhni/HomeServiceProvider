package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.dto.OrderFilterDTO;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.repository.OrderRepository;
import ir.maktab.homeserviceprovider.service.OrderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl
        extends BaseServiceImpl<Order, Long, OrderRepository> implements OrderService {

    @PersistenceContext
    private EntityManager entityManager;

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
    public List<Order> findByCustomerIdAndOrderStatus(Long customerId, OrderStatus orderStatus) {
        try {
            return repository.findByCustomerIdAndOrderStatus(customerId, orderStatus);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public int changeOrderStatus(Long orderId, OrderStatus orderStatus, OrderStatus newOrderStatus) {
        return repository.changeOrderStatus(orderId, orderStatus, newOrderStatus);
    }

    @Override
    public List<Order> findBySubServiceIdAndOrderStatus(Long subServiceId, OrderStatus orderStatusOne, OrderStatus orderStatusTwo) {
        try {
            return repository.findBySubServiceIdAndOrderStatus(subServiceId, orderStatusOne, orderStatusTwo);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Order> ordersFilter(OrderFilterDTO orderDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> orderCriteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = orderCriteriaQuery.from(Order.class);

        createFilters(orderDTO, predicateList, criteriaBuilder, orderRoot);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        orderCriteriaQuery.select(orderRoot).where(predicates);
        return entityManager.createQuery(orderCriteriaQuery).getResultList();
    }

    private void createFilters(OrderFilterDTO orderDTO, List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Order> orderRoot) {
        if (orderDTO.getAddress() != null) {
            String address = "%" + orderDTO.getAddress() + "%";
            predicateList.add(criteriaBuilder.like(orderRoot.get("address"), address));
        }
        if (orderDTO.getDescription() != null) {
            String description = "%" + orderDTO.getDescription() + "%";
            predicateList.add(criteriaBuilder.like(orderRoot.get("description"), description));
        }
        if (orderDTO.getOrderStatus() != null) {
            predicateList.add(criteriaBuilder.equal(orderRoot.get("orderStatus"), orderDTO.getOrderStatus()));
        }
        if (orderDTO.getSubServiceId() != null) {
            predicateList.add(criteriaBuilder.equal(orderRoot.get("subServiceId"), orderDTO.getSubServiceId()));
        }

        if (orderDTO.getMinProposedPrice() == null && orderDTO.getMaxProposedPrice() != null) {
            predicateList.add(criteriaBuilder.lt(orderRoot.get("proposedPrice"), orderDTO.getMaxProposedPrice()));
        }
        if (orderDTO.getMinProposedPrice() != null && orderDTO.getMaxProposedPrice() == null) {
            predicateList.add(criteriaBuilder.gt(orderRoot.get("proposedPrice"), orderDTO.getMinProposedPrice()));
        }
        if (orderDTO.getMinProposedPrice() != null && orderDTO.getMaxProposedPrice() != null) {
            predicateList.add(criteriaBuilder.between(orderRoot.get("proposedPrice"), orderDTO.getMinProposedPrice(), orderDTO.getMaxProposedPrice()));
        }

        if (orderDTO.getMinCreationDate() != null && orderDTO.getMaxCreationDate() != null) {
            predicateList
                    .add(criteriaBuilder
                            .between(orderRoot.get("creationDate"),
                                    LocalDateTime.parse(orderDTO.getMinCreationDate()),
                                    LocalDateTime.parse(orderDTO.getMaxCreationDate())));
        }
        if (orderDTO.getMinUpdateTime() != null && orderDTO.getMaxUpdateTime() != null) {
            predicateList
                    .add(criteriaBuilder
                            .between(orderRoot.get("updateTime"),
                                    LocalDateTime.parse(orderDTO.getMinUpdateTime()),
                                    LocalDateTime.parse(orderDTO.getMaxUpdateTime())));
        }
    }

    @Override
    public int calculateNumberOfRegisteredOrders(Long customerId) {
        return repository.calculateNumberOfRegisteredOrders(customerId);
    }

    @Override
    public int calculateNumberOfRegisteredOrders(Long customerId, OrderStatus orderStatus) {
        return repository.calculateNumberOfRegisteredOrders(customerId, orderStatus);
    }
}
