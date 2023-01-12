package ir.maktab.homeserviceprovider.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.maktab.homeserviceprovider.dto.PersonDTO;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.exception.FileNotFoundException;
import ir.maktab.homeserviceprovider.service.impl.CustomerRegistrationService;
import ir.maktab.homeserviceprovider.service.impl.ExpertRegistrationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final CustomerRegistrationService customerRegistrationService;
    private final ExpertRegistrationService expertRegistrationService;

    public SignUpController(CustomerRegistrationService customerRegistrationService, ExpertRegistrationService expertRegistrationService) {
        this.customerRegistrationService = customerRegistrationService;
        this.expertRegistrationService = expertRegistrationService;
    }

    @PostMapping("/customer")
    public String customerSignup(@Valid @RequestBody PersonDTO personDTO) {
        return customerRegistrationService.register(
                Customer.builder()
                        .firstname(personDTO.getFirstname())
                        .lastname(personDTO.getLastname())
                        .email(personDTO.getEmail())
                        .username(personDTO.getUsername())
                        .password(personDTO.getPassword())
                        .build());
    }

    @GetMapping(path = "customer/confirm")
    public String confirmCustomer(@RequestParam("token") String token) {
        return customerRegistrationService.confirmToken(token);
    }

    @PostMapping("/expert")
    public String expertSignup(@RequestParam("expert") String expert, @RequestParam("image") MultipartFile multipartFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PersonDTO personDTO = objectMapper.readValue(expert, PersonDTO.class);
            return expertRegistrationService.register(Expert.builder()
                            .firstname(personDTO.getFirstname())
                            .lastname(personDTO.getLastname())
                            .email(personDTO.getEmail())
                            .username(personDTO.getUsername())
                            .password(personDTO.getPassword())
                            .image(multipartFile.getBytes())
                            .build(),
                    multipartFile.getOriginalFilename(), multipartFile.getSize());
        } catch (IOException e) {
            throw new FileNotFoundException("this file not found!");
        }
    }

    @GetMapping(path = "expert/confirm")
    public String confirmExpert(@RequestParam("token") String token) {
        return expertRegistrationService.confirmToken(token);
    }

}
