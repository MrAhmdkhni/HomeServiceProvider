package ir.maktab.homeserviceprovider.entity.person;

import ir.maktab.homeserviceprovider.entity.order.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Customer extends Person{

    private Long credit;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @Builder
    public Customer(String firstname, String lastname, String email, String username, String password, Boolean isActive, Role role) {
        super(firstname, lastname, email, username, password, isActive, role);
    }

    @Override
    public String toString() {
        return "Customer {" +
                "firstname='" + getFirstname() + '\'' +
                ", lastname='" + getLastname() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", username='" + getUsername() + '\'' +
                "credit=" + credit +
                "} ";
    }
}
