package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.service.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubServiceRepository extends JpaRepository<SubService, Long> {

    Optional<SubService> findByName(String name);

    int deleteByName(String name);

    @Modifying
    @Query("""
            update SubService ss
            set ss.basePrice = :basePrice, ss.description = :description
            where ss.id = :subServiceId
            """)
    int editBasePriceAndDescription(Long subServiceId, Long basePrice, String description);
}
