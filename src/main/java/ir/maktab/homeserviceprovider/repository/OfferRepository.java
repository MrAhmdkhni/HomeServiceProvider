package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.offer.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query("""
            select o from Offer o
            where o.order.id = :orderId
            order by o.proposedPrice
            """)
    List<Offer> findByOrderIdBasedOnProposedPrice(Long orderId);

    @Query("""
            select o from Offer o
            where o.order.id = :orderId
            order by o.expert.score desc
            """)
    List<Offer> findByOrderIdBasedOnExpertScore(Long orderId);

    @Modifying
    @Query("""
            update Offer o
            set o.isAccept = :isAccept
            where o.id = :offerId
            """)
    int editIsAccept(Long offerId, Boolean isAccept);


}
