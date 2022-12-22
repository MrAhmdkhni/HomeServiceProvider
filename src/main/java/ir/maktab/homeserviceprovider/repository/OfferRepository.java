package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.offer.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByOrderId(Long orderId);

    @Modifying
    @Query("""
            update Offer o
            set o.isAccept = :isAccept
            where o.id = :offerId
            """)
    int editIsAccept(Long offerId, Boolean isAccept);
}
