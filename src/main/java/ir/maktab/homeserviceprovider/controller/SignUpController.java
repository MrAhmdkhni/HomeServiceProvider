package ir.maktab.homeserviceprovider.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.maktab.homeserviceprovider.dto.PersonDTO;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.token.ConfirmationToken;
import ir.maktab.homeserviceprovider.exception.FileNotFoundException;
import ir.maktab.homeserviceprovider.service.ConfirmationTokenService;
import ir.maktab.homeserviceprovider.service.CustomerService;
import ir.maktab.homeserviceprovider.service.EmailSenderService;
import ir.maktab.homeserviceprovider.service.ExpertService;
import jakarta.validation.Valid;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final CustomerService customerService;
    private final ExpertService expertService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;

    public SignUpController(CustomerService customerService, ExpertService expertService, ConfirmationTokenService confirmationTokenService, EmailSenderService emailSenderService) {
        this.customerService = customerService;
        this.expertService = expertService;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/customer")
    public void customerSignup(@Valid @RequestBody PersonDTO personDTO) {
        customerService.signUpWithValidation(
                Customer.builder()
                        .firstname(personDTO.getFirstname())
                        .lastname(personDTO.getLastname())
                        .email(personDTO.getEmail())
                        .username(personDTO.getUsername())
                        .password(personDTO.getPassword())
                        .build()
        );
    }

    @PostMapping("/expert")
    public void expertSignup(@RequestParam("expert") String expert, @RequestParam("image") MultipartFile multipartFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PersonDTO personDTO = objectMapper.readValue(expert, PersonDTO.class);
            expertService.signUpWithValidation(
                    Expert.builder()
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

    @PostMapping("/register")
    public void registerUser(ModelAndView modelAndView, @Valid @RequestBody PersonDTO personDTO) {
        Customer customer = Customer.builder()
                .firstname(personDTO.getFirstname())
                .lastname(personDTO.getLastname())
                .email(personDTO.getEmail())
                .username(personDTO.getUsername())
                .password(personDTO.getPassword())
                .build();

        Optional<Customer> existingCustomer = customerService.findByEmail(customer.getEmail());// find by email
        if (existingCustomer.isPresent()) {
            modelAndView.addObject("message", "This email already exists!");
            modelAndView.setViewName("error");
        } else {
            customerService.saveOrUpdate(customer);

            ConfirmationToken confirmationToken = new ConfirmationToken(customer);

            confirmationTokenService.saveOrUpdate(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(customer.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("popularamirhosein@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                    + "http://localhost:8080/signup/confirm-account?token=" + confirmationToken.getConfirmationToken());

            emailSenderService.sendEmail(mailMessage);

            modelAndView.addObject("email", customer.getEmail());

            modelAndView.setViewName("successfulRegistration");
        }
    }

    @GetMapping("/confirm-customer/{confirmationToken}")
    public void confirmCustomerAccount(@PathVariable String confirmationToken) {
        customerService.confirmCustomerAccount(confirmationToken);
    }

    @GetMapping("/confirm-expert/{confirmationToken}")
    public void confirmExpertAccount(@PathVariable String confirmationToken) {
        expertService.confirmExpertAccount(confirmationToken);
    }
}
