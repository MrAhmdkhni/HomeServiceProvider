package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long> {

    Optional<Expert> findByEmail(String email);

    Optional<Expert> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("""
            update Expert e
            set e.password = :newPassword
            where e.id = :expertId
            """)
    int editPassword(Long expertId, String newPassword);

    @Modifying
    @Query("""
            update Expert e
            set e.expertStatus = :expertStatus
            where e.id = :expertId
            """)
    int editExpertStatus(Long expertId, ExpertStatus expertStatus);
}
