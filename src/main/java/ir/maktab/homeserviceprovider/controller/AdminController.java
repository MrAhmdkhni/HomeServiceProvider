package ir.maktab.homeserviceprovider.controller;

import ir.maktab.homeserviceprovider.dto.*;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final ModelMapper mapper;

    public AdminController(AdminService adminService, ModelMapper mapper) {
        this.adminService = adminService;
        this.mapper = mapper;
    }


    @PostMapping("/addMainService")
    public void addMainService(@RequestBody MainServiceDTO mainServiceDTO) {
        adminService.addMainService(MainService.builder().name(mainServiceDTO.getName()).build());
    }

    @DeleteMapping("/deleteMainService/{id}")
    public void deleteMainService(@PathVariable Long id) {
        adminService.deleteMainService(id);
    }

    @PostMapping("/addSubService")
    public void addSubService(@RequestBody MainServiceSubServiceDTO mainServiceSubServiceDTO) {
        adminService.addSubService(mainServiceSubServiceDTO.getMainServiceId(),
                SubService.builder()
                        .name(mainServiceSubServiceDTO.getName())
                        .basePrice(mainServiceSubServiceDTO.getBasePrice())
                        .description(mainServiceSubServiceDTO.getDescription())
                        .build());
    }

    @DeleteMapping("/deleteSubService/{id}")
    public void deleteSubService(@PathVariable Long id) {
        adminService.deleteSubService(id);
    }

    @PostMapping("/addExpertToSubService/{subServiceId}/{expertId}")
    public void addExpertToSubService(@PathVariable Long subServiceId, @PathVariable Long expertId) {
        adminService.addExpertToSubService(subServiceId, expertId);
    }

    @DeleteMapping("/deleteExpertFromSubService/{subServiceId}/{expertId}")
    public void deleteExpertFromSubService(@PathVariable Long subServiceId, @PathVariable Long expertId) {
        adminService.deleteExpertFromSubService(subServiceId, expertId);
    }

    @GetMapping("/findAllMainService")
    public List<MainServiceDTO> findAllMainService() {
        List<MainServiceDTO> mainServiceDTOS = new ArrayList<>();
        adminService.findAllMainService().forEach(mainService ->
                mainServiceDTOS.add(mapper.map(mainService, MainServiceDTO.class))
        );
        return mainServiceDTOS;
    }

    @GetMapping("/findAllSubService")
    public List<SubServiceDTO> findAllSubService() {
        List<SubServiceDTO> subServiceDTOS = new ArrayList<>();
        adminService.findAllSubService().forEach(subService ->
                subServiceDTOS.add(mapper.map(subService, SubServiceDTO.class))
        );
        return subServiceDTOS;
    }

    @PutMapping("/editSubServiceBasePriceAndDescription")
    public int editSubServiceBasePriceAndDescription(@RequestBody SubServiceDetailDTO subServiceDetailDTO) {
        return adminService.editSubServiceBasePriceAndDescription(
                subServiceDetailDTO.getSubServiceId(),
                subServiceDetailDTO.getBasePrice(),
                subServiceDetailDTO.getDescription());
    }

    @GetMapping("/findAllExpert")
    public List<ExpertDTO> findAllExpert() {
        List<ExpertDTO> expertDTOS = new ArrayList<>();
        adminService.findAllExpert().forEach(expert ->
                expertDTOS.add(mapper.map(expert, ExpertDTO.class))
        );
        return expertDTOS;
    }

    @PutMapping("/changeExpertStatus")
    public int changeExpertStatus(@RequestBody ExpertStatusDTO expertStatusDTO) {
        return adminService.changeExpertStatus(expertStatusDTO.getExpertId(), expertStatusDTO.getExpertStatus());
    }

    @PutMapping("/checkExpertDelay/{offerId}")
    public boolean checkExpertDelayForDoingWork(@PathVariable Long offerId) {
        return adminService.checkExpertDelayForDoingWork(offerId);
    }

    @PutMapping("/changeExpertActivation")
    public int changeExpertActivation(@RequestBody ExpertActivationDTO expertActivationDTO) {
        return adminService.changeExpertActivation(expertActivationDTO.getExpertId(), expertActivationDTO.getIsActive());
    }

    @PostMapping("/expertsFilter")
    public List<ExpertDTO> expertsFilter(@RequestBody ExpertFilterDTO expertFilterDTO) {
        List<ExpertDTO> expertDTOS = new ArrayList<>();
        adminService.expertsFilter(expertFilterDTO)
                .forEach(expert ->
                        expertDTOS.add(mapper.map(expert, ExpertDTO.class)));
        return expertDTOS;
    }

    @PostMapping("/customersFilter")
    public List<PersonDTO> customersFilter(@RequestBody CustomerFilterDTO customerDTO) {
        List<PersonDTO> personDTOS = new ArrayList<>();
        adminService.customersFilter(customerDTO).forEach(customer ->
                personDTOS.add(mapper.map(customer, PersonDTO.class))
        );
        return personDTOS;
    }

    @Transactional
    @GetMapping("/viewSubServiceExperts/{subServiceId}")
    public Set<ExpertDTO> viewSubServiceExperts(@PathVariable Long subServiceId) {
        Set<ExpertDTO> expertDTOS = new HashSet<>();
        adminService.viewSubServiceExperts(subServiceId)
                .forEach(expert ->
                        expertDTOS.add(mapper.map(expert, ExpertDTO.class)));
        return expertDTOS;
    }

    @PostMapping("/ordersFilter")
    public List<OrderDTO> ordersFilter(@RequestBody OrderFilterDTO orderFilterDTO) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        adminService.ordersFilter(orderFilterDTO)
                .forEach(order ->
                        orderDTOS.add(mapper.map(order, OrderDTO.class)));
        return orderDTOS;
    }

    @GetMapping("/viewExpertOrderHistory/{expertId}/{isAccept}")
    public List<OrderDTO> viewExpertOrderHistory(@PathVariable Long expertId, @PathVariable boolean isAccept) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        adminService.viewExpertOrderHistory(expertId, isAccept)
                .forEach(
                        order -> orderDTOS.add(
                                mapper.map(order, OrderDTO.class)));
        return orderDTOS;
    }

    @PostMapping("/viewExpertOrderHistory")
    public List<OrderDTO> viewExpertOrderHistory(@RequestBody ExpertOrderHistoryDTO expertOrderHistoryDTO) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        adminService.viewExpertOrderHistory(
                        expertOrderHistoryDTO.getExpertId(),
                        expertOrderHistoryDTO.getIsAccept(),
                        expertOrderHistoryDTO.getOrderStatus())
                .forEach(
                        order -> orderDTOS.add(
                                mapper.map(order, OrderDTO.class)));
        return orderDTOS;
    }

    @GetMapping("/viewCustomerOrderHistory/{customerId}")
    public List<OrderDTO> viewCustomerOrderHistory(@PathVariable Long customerId) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        adminService.viewCustomerOrderHistory(customerId)
                .forEach(
                        order -> orderDTOS.add(
                                mapper.map(order, OrderDTO.class))
                );
        return orderDTOS;
    }

    @PostMapping("/viewCustomerOrderHistory")
    public List<OrderDTO> viewCustomerOrderHistory(@RequestBody CustomerOrderStatusDTO customerOrderStatusDTO) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        adminService.viewCustomerOrderHistory(
                        customerOrderStatusDTO.getCustomerId(),
                        customerOrderStatusDTO.getOrderStatus())
                .forEach(order -> orderDTOS.add(mapper.map(order, OrderDTO.class)));
        return orderDTOS;
    }

    @GetMapping("/calculateNumberOfRegisteredOrders/{customerId}")
    public int calculateNumberOfRegisteredOrders(@PathVariable Long customerId) {
        return adminService.calculateNumberOfRegisteredOrders(customerId);
    }

    @PostMapping("/calculateNumberOfRegisteredOrders")
    public int calculateNumberOfRegisteredOrders(@RequestBody CustomerOrderStatusDTO customerOrderStatusDTO) {
        return adminService.calculateNumberOfRegisteredOrders(
                customerOrderStatusDTO.getCustomerId(),
                customerOrderStatusDTO.getOrderStatus());
    }

    @GetMapping("/calculateNumberOfPlacedOrders/{expertId}")
    public int calculateNumberOfPlacedOrders(@PathVariable Long expertId) {
        return adminService.calculateNumberOfPlacedOrders(expertId);
    }

    @GetMapping("/calculateNumberOfPlacedOrders/{expertId}/{isAccept}")
    public int calculateNumberOfPlacedOrders(@PathVariable Long expertId, @PathVariable Boolean isAccept) {
        return adminService.calculateNumberOfPlacedOrders(expertId, isAccept);
    }
}
