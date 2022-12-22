package ir.maktab.homeserviceprovider.entity.order;

import ir.maktab.homeserviceprovider.base.entity.BaseEntity;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
public class Order extends BaseEntity<Long> {

    @Column(name = "proposed-price")
    private Long proposedPrice;
    private String description;
    @Column(name = "execution_time")
    private LocalDateTime executionTime;
    private String address;
    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Customer customer;
    @ManyToOne(cascade = CascadeType.MERGE)
    private SubService subService;
    @OneToMany(mappedBy = "order")
    private List<Offer> offers  = new ArrayList<>();

    public Order(Long proposedPrice, String description, LocalDateTime executionTime, String address, OrderStatus orderStatus, Customer customer, SubService subService) {
        this.proposedPrice = proposedPrice;
        this.description = description;
        this.executionTime = executionTime;
        this.address = address;
        this.orderStatus = orderStatus;
        this.customer = customer;
        this.subService = subService;
    }

    public Order(Long proposedPrice, String description, LocalDateTime executionTime, String address) {
        this.proposedPrice = proposedPrice;
        this.description = description;
        this.executionTime = executionTime;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Order {" +
                "proposedPrice=" + proposedPrice +
                ", description='" + description + '\'' +
                ", executionTime=" + executionTime +
                ", address='" + address + '\'' +
                ", orderStatus=" + orderStatus +
                ", creationDate=" + getCreationDate() +
                "} ";
    }
}
