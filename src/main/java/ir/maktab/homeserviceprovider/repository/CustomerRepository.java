package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.person.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("""
            update Customer c
            set c.password = :newPassword
            where c.id = :customerId
            """)
    int editPassword(Long customerId, String newPassword);

    boolean existsByEmail(String email);
}
