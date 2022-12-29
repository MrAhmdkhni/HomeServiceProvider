package ir.maktab.homeserviceprovider.entity.service;

import ir.maktab.homeserviceprovider.base.entity.BaseEntity;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubService extends BaseEntity<Long> implements Serializable {

    @Column(nullable = false)
    private String name;
    private Long basePrice;
    private String description;
    @ManyToOne
    private MainService mainService;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Expert> experts = new HashSet<>();
    @OneToMany(mappedBy = "subService", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public SubService(String name, Long basePrice, String description) {
        this.name = name;
        this.basePrice = basePrice;
        this.description = description;
    }

    public SubService(String name, Long basePrice, String description, MainService mainService) {
        this.name = name;
        this.basePrice = basePrice;
        this.description = description;
        this.mainService = mainService;
    }

    @Override
    public String toString() {
        return "SubServiceService{" +
                "name='" + name + '\'' +
                ", basePrice=" + basePrice +
                ", description='" + description + '\'' +
                ", main-service=" + mainService.getName() +
                '}';
    }


}
