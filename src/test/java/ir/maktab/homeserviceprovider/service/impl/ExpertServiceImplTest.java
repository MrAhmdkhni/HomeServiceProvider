package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.offer.TimeType;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpertServiceImplTest {

    @Autowired
    private ExpertService expertService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SubServiceService subServiceService;
    @Autowired
    private AdminService adminService;


    @Test
    void changeExpertStatus() {
        Expert expert = new Expert("ggg1", "ggg1", "09999999222", "ggg1@gmail.com", "11AAaa22");
        expertService.saveOrUpdate(expert);
        int affectedRows = expertService.changeExpertStatus(expert.getId(), ExpertStatus.AWAITING);
        assertEquals(1, affectedRows);
    }

    @Test
    void signUp() {
        Expert expert = new Expert("ggg", "ggg", "09999999232", "ggg@gmail.com", "11AAaa22");
        FileInputStream image = null;
        try {
            image = new FileInputStream("test.jpg");
            expert.setImage(image.readAllBytes());
            image.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        expertService.signUp(expert, "test.jpg", 87L);
        Optional<Expert> optionalExpert = expertService.findById(expert.getId());
        assertEquals(expert.getEmail(), optionalExpert.get().getEmail());
    }

    @Test
    void signUpTest() {
        Optional<Expert> optionalExpert = expertService.findById(28L);
        String fileName = "demo.jpg";
        FileOutputStream fileOutput = null;
        try {
            fileOutput = new FileOutputStream(fileName);
            Blob blob = new SerialBlob(optionalExpert.get().getImage());
            int length = (int) blob.length();
            byte[] bytes = blob.getBytes(1, length);
            fileOutput.write(bytes, 0, length);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addOfferForOrder() {
        Expert expert = new Expert("ggg2", "ggg2", "09999999333", "ggg2@gmail.com", "11AAaa22");
        expert.setExpertStatus(ExpertStatus.CONFIRMED);

        Order order  = new Order(10L, "---", LocalDateTime.of(1401, 11, 15, 18, 30), "cccc");
        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT_SUGGESTION);

        SubService subService = new SubService("sub-99", 12000L, "---");
        subServiceService.saveOrUpdate(subService);

        order.setSubService(subService);

        orderService.saveOrUpdate(order);
        expertService.saveOrUpdate(expert);
        adminService.addExpertToSubService(subService.getId(), expert.getId());

        Offer offer = new Offer("alakiii", 60000L, TimeType.MINUTE, 50, LocalDateTime.of(LocalDate.parse("2022-12-28"), LocalTime.parse("14:57:44")));
        expertService.addOfferForOrder(expert.getId(), order.getId(), offer);
        Optional<Offer> optionalOffer = offerService.findById(offer.getId());
        assertEquals(offer.getOffer(), optionalOffer.get().getOffer());
    }
}