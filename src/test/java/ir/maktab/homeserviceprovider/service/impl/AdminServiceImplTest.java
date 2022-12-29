package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.service.AdminService;
import ir.maktab.homeserviceprovider.service.ExpertService;
import ir.maktab.homeserviceprovider.service.MainServiceService;
import ir.maktab.homeserviceprovider.service.SubServiceService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    void addMainService() {
        MainService mainService = new MainService("main-1");
        adminService.addMainService(mainService);
        Optional<MainService> optionalMainService = mainServiceService.findById(mainService.getId());
        assertEquals(mainService.getName(), optionalMainService.get().getName());
    }

    @Test
    @Order(2)
    void addSubService() {
        MainService mainService = new MainService("main-2");
        mainServiceService.saveOrUpdate(mainService);
        SubService subService = new SubService("sub-1", 12000L, "---");
        adminService.addSubService(mainService.getId(), subService);
        Optional<SubService> optionalSubService = subServiceService.findById(subService.getId());
        assertEquals(subService.getName(), optionalSubService.get().getName());
    }

    @Test
    @Order(3)
    void addExpertToSubService() {
        Expert expert = new Expert("aaa", "bbb", "09999999996", "aaa@gmail.com", "11MMnn11");
        expert.setExpertStatus(ExpertStatus.CONFIRMED);
        expertService.saveOrUpdate(expert);
        SubService subService = new SubService("sub-2", 12000L, "---");
        subServiceService.saveOrUpdate(subService);

        adminService.addExpertToSubService(subService.getId(), expert.getId());
        Set<SubService> expertSubServices = expert.getSubServices();
        expertSubServices.forEach(newSubService -> {
            if (newSubService.getId() == subService.getId())
                assertEquals(subService.getId(), newSubService.getId());
        });
    }


    @Test
    @Order(4)
    void editSubServiceBasePriceAndDescription() {
        SubService subService = new SubService("sub-3", 12000L, "---");
        subServiceService.saveOrUpdate(subService);
        int affectedRows = adminService.editSubServiceBasePriceAndDescription(subService.getId(), 999L, "radio");
        assertEquals(1, affectedRows);
    }

    @Test
    @Order(5)
    void changeExpertStatus() {
        Expert expert = new Expert("kkk", "kkk", "09999999956", "kkk@gmail.com", "11MMnn11");
        expertService.saveOrUpdate(expert);
        int affectedRows = adminService.changeExpertStatus(expert.getId(), ExpertStatus.CONFIRMED);
        assertEquals(1, affectedRows);
    }



    @Test
    @Order(6)
    void findAllMainServiceAndDeleteMainService() {
        MainService mainService = new MainService("main-3");
        mainServiceService.saveOrUpdate(mainService);
        List<MainService> mainService1 = adminService.findAllMainService();
        adminService.deleteMainService(mainService.getId());
        List<MainService> mainServices2 = adminService.findAllMainService();
        assertEquals(mainService1.size()-1, mainServices2.size());
    }

    @Test
    @Order(7)
    void findAllSubServiceAndDeleteSubService() {
        SubService subService = new SubService("sub-4", 12000L, "---");
        subServiceService.saveOrUpdate(subService);
        List<SubService> subServices1 = adminService.findAllSubService();
        adminService.deleteSubService(subService.getId());
        List<SubService> subServices2 = adminService.findAllSubService();
        assertEquals(subServices1.size()-1, subServices2.size());
    }

    @Test
    @Order(8)
    void findAllExpert() {
        Expert expert = new Expert("sss", "sss", "09999999936", "sss@gmail.com", "11MMnn11");
        expertService.saveOrUpdate(expert);
        List<Expert> experts1 = adminService.findAllExpert();
        expertService.delete(expert);
        List<Expert> experts2 = adminService.findAllExpert();
        assertEquals(experts1.size()-1, experts2.size());
    }
}