package ir.maktab.homeserviceprovider.controller;

import ir.maktab.homeserviceprovider.dto.*;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
        adminService.findAllMainService().forEach(mainService -> {
            mainServiceDTOS.add(new MainServiceDTO(mainService.getName()));
        });
        return mainServiceDTOS;
    }

    @GetMapping("/findAllSubService")
    public List<SubServiceDTO> findAllSubService() {
        List<SubServiceDTO> subServiceDTOS = new ArrayList<>();
        adminService.findAllSubService().forEach(subService -> {
            subServiceDTOS.add(new SubServiceDTO(
                    subService.getName(),
                    subService.getBasePrice(),
                    subService.getDescription(),
                    subService.getMainService().getName()));
        });
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
        adminService.findAllExpert().forEach(expert -> {
            expertDTOS.add(new ExpertDTO(
                    expert.getFirstname(),
                    expert.getLastname(),
                    expert.getPhoneNumber(),
                    expert.getEmail(),
                    expert.getScore(),
                    expert.getExpertStatus(),
                    expert.getCredit(),
                    expert.getIsActive()));
        });
        return expertDTOS;
    }

    @PutMapping("/changeExpertStatus")
    public int changeExpertStatus(@RequestBody ExpertStatusDTO expertStatusDTO) {
        return adminService.changeExpertStatus(expertStatusDTO.getExpertId(), expertStatusDTO.getExpertStatus());
    }

    @PutMapping("/checkExpertDelay/{offerId}")
    public boolean checkExpertDelayForDoingWork(@PathVariable Long offerId){
        return adminService.checkExpertDelayForDoingWork(offerId);
    }

    @PutMapping("/changeExpertActivation")
    public int changeExpertActivation(@RequestBody ExpertActivationDTO expertActivationDTO) {
        return adminService.changeExpertActivation(expertActivationDTO.getExpertId(), expertActivationDTO.getIsActive());
    }

    @GetMapping("/expertsFilters")
    public List<ExpertFilterDTO> expertsFilters(@RequestBody ExpertFilterDTO expertDTO){
        List<ExpertFilterDTO> expertFilterDTOS = new ArrayList<>();
        adminService.expertsFilter(expertDTO).forEach(expert -> {
            expertFilterDTOS.add(new ExpertFilterDTO(
                    expertDTO.getFirstname(),
                    expertDTO.getLastname(),
                    expertDTO.getPhoneNumber(),
                    expertDTO.getEmail(),
                    expertDTO.getMaxScore(),
                    expertDTO.getMinScore(),
                    expertDTO.getExpertStatus(),
                    expertDTO.getMaxCredit(),
                    expertDTO.getMinCredit(),
                    expertDTO.getIsActive()));
        });
        return expertFilterDTOS;
    }

    @GetMapping("/expertsFilter")
    public List<ExpertDTO> expertsFilter(@RequestBody ExpertFilterDTO expertFilterDTO){
        List<ExpertDTO> expertDTOS = new ArrayList<>();
        adminService.expertsFilter(expertFilterDTO).forEach(expertDTO -> {
            expertDTOS.add(new ExpertDTO(
                    expertDTO.getFirstname(),
                    expertDTO.getLastname(),
                    expertDTO.getPhoneNumber(),
                    expertDTO.getEmail(),
                    expertDTO.getScore(),
                    expertDTO.getExpertStatus(),
                    expertDTO.getCredit(),
                    expertDTO.getIsActive()));
        });
        return expertDTOS;
    }

    @GetMapping("/customersFilter")
    public List<CustomerFilterDTO> customersFilter(@RequestBody CustomerFilterDTO customerDTO){
        List<CustomerFilterDTO> customerFilterDTOS = new ArrayList<>();
        adminService.customersFilter(customerDTO).forEach(customer -> {
            customerFilterDTOS.add(new CustomerFilterDTO(
                    customerDTO.getFirstname(),
                    customerDTO.getLastname(),
                    customerDTO.getPhoneNumber(),
                    customerDTO.getEmail(),
                    customerDTO.getMaxCredit(),
                    customerDTO.getMinCredit()));
        });
        return customerFilterDTOS;
    }
}
