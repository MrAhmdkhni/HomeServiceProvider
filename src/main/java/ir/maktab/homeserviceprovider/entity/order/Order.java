package ir.maktab.homeserviceprovider.entity.order;

import ir.maktab.homeserviceprovider.base.entity.BaseEntity;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity<Long> {

    private Long proposedPrice;
    private String description;
    private LocalDateTime executionTime;
    private String address;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
    @UpdateTimestamp
    private LocalDateTime updateTime;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Customer customer;
    @ManyToOne(cascade = CascadeType.MERGE)
    private SubService subService;
    @OneToMany(mappedBy = "order")
    private List<Offer> offers  = new ArrayList<>();

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
