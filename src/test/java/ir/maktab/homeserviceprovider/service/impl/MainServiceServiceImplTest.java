package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.service.MainServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MainServiceServiceImplTest {

    @Autowired
    MainServiceService mainServiceService;

    @Test
    void findByName() {
        Optional<MainService> optionalSubService = mainServiceService.findByName("Home Appliance");
        assertTrue(optionalSubService.isPresent());
    }

    @Test
    void deleteByName() {
        int affectedRows = mainServiceService.deleteByName("Home Appliance");
        assertEquals(1, affectedRows);
    }
}