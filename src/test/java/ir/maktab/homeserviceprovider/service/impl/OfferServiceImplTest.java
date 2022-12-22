package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.service.OfferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OfferServiceImplTest {

    @Autowired
    private OfferService offerService;

    @Test
    void findByOrderId() {
        List<Offer> offers = offerService.findByOrderId(10L);
        Optional<Offer> optionalOffer = offerService.findById(3L);
        assertEquals(offers.get(0).getCreationDate(), optionalOffer.get().getCreationDate());
    }

    @Test
    void editIsAccept() {
        int affectedRows = offerService.editIsAccept(1L, true);
        assertEquals(1, affectedRows);
    }
}