package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.service.AdminService;
import ir.maktab.homeserviceprovider.service.ExpertService;
import ir.maktab.homeserviceprovider.service.MainServiceService;
import ir.maktab.homeserviceprovider.service.SubServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminServiceImplTest {

    @Autowired
    private AdminService adminService;
    @Autowired
    private MainServiceService mainServiceService;
    @Autowired
    private SubServiceService subServiceService;
    @Autowired
    private ExpertService expertService;

    @Test
    void addMainService() {
        MainService mainService = new MainService("cleaning and hygiene");
        adminService.addMainService(mainService);
        Optional<MainService> optionalMainService = mainServiceService.findById(mainService.getId());
        optionalMainService.ifPresent(newMainService -> {
            assertEquals("cleaning and hygiene", newMainService.getName());
        });
    }

    @Test
    void deleteMainService() {
        Optional<MainService> mainService = mainServiceService.findByName("vehicle");
        int affectedRows = adminService.deleteMainService(mainService.get());
        assertEquals(1, affectedRows);
    }

    @Test
    void addSubService() {
        SubService subService = new SubService("cleaning", 12000L, "cleaning");
        adminService.addSubService(1L, subService);
        Optional<SubService> optionalSubService = subServiceService.findById(subService.getId());
        optionalSubService.ifPresent(newSubService -> {
            assertEquals("cleaning", newSubService.getName());
        });
    }

    @Test
    void deleteSubService() {
        Optional<SubService> subService = subServiceService.findByName("radio");
        int affectedRows = adminService.deleteSubService(subService.get());
        assertEquals(1, affectedRows);
    }

    @Test
    void addExpertToSubService() {
        adminService.addExpertToSubService(2L, 3L);
        Optional<Expert> expert = expertService.findById(3L);
        Set<SubService> expertSubServices = expert.get().getSubServices();
        expertSubServices.forEach(subService -> {
            if (subService.getId() == 2)
                assertEquals(2L, subService.getId());
        });
    }

    @Test
    void deleteExpertFromSubService() {
        adminService.deleteExpertFromSubService(2L, 3L);
        Optional<Expert> expert = expertService.findById(3L);
        Set<SubService> expertSubServices = expert.get().getSubServices();
        expertSubServices.forEach(subService -> {
            assertNotEquals(2L, subService.getId());
        });
    }

    @Test
    void findAllMainService() {
        List<MainService> mainServices = adminService.findAllMainService();
        Optional<MainService> optionalMainService = mainServiceService.findById(1L);
        assertEquals(mainServices.get(0).getId(), optionalMainService.get().getId());
    }

    @Test
    void findAllSubService() {
        List<SubService> subServices = adminService.findAllSubService();
        Optional<SubService> optionalSubService = subServiceService.findById(1L);
        assertEquals(subServices.get(0).getId(), optionalSubService.get().getId());
    }

    @Test
    void editSubServiceBasePriceAndDescription() {
        int affectedRows = adminService.editSubServiceBasePriceAndDescription(2L, 999L, "radio");
        assertEquals(1, affectedRows);
    }

    @Test
    void findAllExpert() {
        List<Expert> experts = adminService.findAllExpert();
        Optional<Expert> optionalExpert = expertService.findById(5L);
        assertEquals(experts.get(0).getId(), optionalExpert.get().getId());
    }

    @Test
    void changeExpertStatus() {
        int affectedRows = adminService.changeExpertStatus(6L, ExpertStatus.CONFIRMED);
        assertEquals(1, affectedRows);
    }
}