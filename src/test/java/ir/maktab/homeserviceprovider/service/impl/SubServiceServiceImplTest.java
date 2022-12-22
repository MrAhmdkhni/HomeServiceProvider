package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.service.SubServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubServiceServiceImplTest {

    @Autowired
    private SubServiceService subServiceService;

    @Test
    void findByName() {
        Optional<SubService> optionalSubService = subServiceService.findByName("TV");
        assertTrue(optionalSubService.isPresent());
    }

    @Test
    void deleteByName() {
        int affectedRows = subServiceService.deleteByName("pc");
        assertEquals(1, affectedRows);
    }

    @Test
    void editBasePriceAndDescription() {
        int affectedRows = subServiceService.editBasePriceAndDescription(1L, 30000L, "new description");
        assertEquals(1, affectedRows);
    }
}