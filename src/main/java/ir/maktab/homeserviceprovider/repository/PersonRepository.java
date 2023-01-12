package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update Person p set p.isActive = true where p.email=?1")
    int activePerson(String email);
}
