package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.offer.TimeType;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import ir.maktab.homeserviceprovider.service.ExpertService;
import ir.maktab.homeserviceprovider.service.OfferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ExpertServiceImplTest {

    @Autowired
    private ExpertService expertService;
    @Autowired
    private OfferService offerService;

    @Test
    void findByEmail() {
        Optional<Expert> optionalExpert = expertService.findByEmail("amir@gmail.com");
        assertTrue(optionalExpert.isPresent());
    }

    @Test
    void findByPhoneNumber() {
        Optional<Expert> optionalExpert = expertService.findByPhoneNumber("09999999999");
        assertTrue(optionalExpert.isPresent());
    }

    @Test
    void editPassword() {
        int affectedRows = expertService.editPassword(3L, "3333KKkk");
        assertEquals(1, affectedRows);
    }

    @Test
    void editExpertStatus() {
        int affectedRows = expertService.editExpertStatus(3L, ExpertStatus.AWAITING);
        assertEquals(1, affectedRows);
    }

    @Test
    void signUp() {
        File file = new File("test.jpg");
        Expert expert = new Expert("shahab", "moradi", "09999999999","shahab@gmail.com", "11AAaa22");
        expertService.signUp(expert, file);
        Optional<Expert> optionalExpert = expertService.findById(expert.getId());
        assertEquals(expert.getEmail(), optionalExpert.get().getEmail());
    }

    @Test
    void addOfferForOrder() {
        Offer offer = new Offer("alakiii", 60000L, TimeType.MINUTE, 50);
        expertService.addOfferForOrder(5L, 9L, offer);
        Optional<Offer> optionalOffer = offerService.findById(offer.getId());
        assertEquals(offer.getOffer(), optionalOffer.get().getOffer());
    }
}