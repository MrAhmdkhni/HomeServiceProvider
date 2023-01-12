package ir.maktab.homeserviceprovider.controller;

import ir.maktab.homeserviceprovider.dto.*;
import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.service.CustomerService;
import ir.maktab.homeserviceprovider.util.ReCaptchaResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;

    public CustomerController(CustomerService customerService, RestTemplate restTemplate, ModelMapper mapper) {
        this.customerService = customerService;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }


    @GetMapping("/findByEmail/{email}")
    @ResponseBody
    public PersonDTO findByEmail(@PathVariable String email) {
        Customer customer = customerService.findByEmail(email).get();
        return mapper.map(customer, PersonDTO.class);
    }

    @GetMapping("/findByUsername/{username}")
    @ResponseBody
    public PersonDTO findByUsername(@PathVariable String username) {
        Customer customer = customerService.findByUsername(username).get();
        return mapper.map(customer, PersonDTO.class);
    }

    @GetMapping("/existsByEmail/{email}")
    @ResponseBody
    public boolean existsByEmail(@PathVariable String email) {
        return customerService.existsByEmail(email);
    }

    @PostMapping("/signup")
    @ResponseBody
    public void singUp(@Valid @RequestBody PersonDTO personDTO) {
        customerService.signUp(
                Customer.builder()
                        .firstname(personDTO.getFirstname())
                        .lastname(personDTO.getLastname())
                        .email(personDTO.getEmail())
                        .username(personDTO.getUsername())
                        .password(personDTO.getPassword())
                        .build()
        );
    }

    @PostMapping("/editPassword")
    @ResponseBody
    public int editPassword(@Valid @RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        Customer authenticatedCustomer = (Customer) authentication.getPrincipal();
        return customerService.editPassword(authenticatedCustomer.getId(), passwordDTO.getPassword());
    }

    @GetMapping("/findAllMainService")
    @ResponseBody
    public List<MainServiceDTO> findAllMainService() {
        List<MainServiceDTO> mainServiceDTOS = new ArrayList<>();
        customerService.findAllMainService().forEach(mainService ->
                mainServiceDTOS.add(mapper.map(mainService, MainServiceDTO.class))
        );
        return mainServiceDTOS;
    }

    @GetMapping("/findAllSubService")
    @ResponseBody
    public List<SubServiceDTO> findAllSubService() {
        List<SubServiceDTO> subServiceDTOS = new ArrayList<>();
        customerService.findAllSubService().forEach(subService ->
                subServiceDTOS.add(mapper.map(subService, SubServiceDTO.class))
        );
        return subServiceDTOS;
    }

    @PostMapping("/addOrder")
    @ResponseBody
    public void addOrder(@RequestBody SubServiceOrderDTO subServiceOrderDTO, Authentication authentication) {
        Customer authenticatedCustomer = (Customer) authentication.getPrincipal();
        customerService.addOrder(authenticatedCustomer.getId(), subServiceOrderDTO.getSubServiceId(),
                Order.builder()
                        .proposedPrice(subServiceOrderDTO.getProposedPrice())
                        .description(subServiceOrderDTO.getDescription())
                        .executionTime(LocalDateTime.of(
                                LocalDate.parse(subServiceOrderDTO.getExecutionDate()),
                                LocalTime.parse(subServiceOrderDTO.getExecutionTime())))
                        .address(subServiceOrderDTO.getAddress())
                        .build());
    }

    @PostMapping("/addComment")
    @ResponseBody
    public void addCommentForExpertPerformance(@RequestBody OrderExpertCommentDTO orderExpertCommentDTO) {
        customerService.addCommentForExpertPerformance(orderExpertCommentDTO.getOrderId(), orderExpertCommentDTO.getExpertId(),
                Comment.builder()
                        .score(orderExpertCommentDTO.getScore())
                        .comment(orderExpertCommentDTO.getComment())
                        .build());
    }

    @GetMapping("/viewOffersBaseOnProposedPrice/{orderId}")
    @ResponseBody
    public List<OfferDTO> viewOrderOffersBaseOnProposedPrice(@PathVariable Long orderId) {
        List<OfferDTO> offerDTOS = new ArrayList<>();
        customerService.viewOrderOffersBaseOnProposedPrice(orderId).forEach(offer -> {
            offerDTOS.add(new OfferDTO(offer.getProposedPrice(),
                    offer.getTimeType(),
                    offer.getDurationTime(),
                    offer.getExpert().getScore(),
                    offer.getEndTime()));
        });
        return offerDTOS;
    }

    @GetMapping("/viewOffersBaseOnExpertScore/{orderId}")
    @ResponseBody
    public List<OfferDTO> viewOrderOffersBaseOnExpertScore(@PathVariable Long orderId) {
        List<OfferDTO> offerDTOS = new ArrayList<>();
        customerService.viewOrderOffersBaseOnExpertScore(orderId).forEach(offer -> {
            offerDTOS.add(new OfferDTO(offer.getProposedPrice(),
                    offer.getTimeType(),
                    offer.getDurationTime(),
                    offer.getExpert().getScore(),
                    offer.getEndTime()));
        });
        return offerDTOS;
    }

    @PutMapping("/selectOffer/{offerId}")
    @ResponseBody
    public void selectOffer(@PathVariable Long offerId) {
        customerService.selectOffer(offerId);
    }

    @PutMapping("/changeOrderStatus/afterExpertComes/{orderId}")
    @ResponseBody
    public int changeOrderStatusAfterExpertComes(@PathVariable Long orderId) {
        return customerService.changeOrderStatusAfterExpertComes(orderId);
    }

    @PutMapping("/changeOrderStatus/afterStarted/{orderId}")
    @ResponseBody
    public int changeOrderStatusAfterStarted(@PathVariable Long orderId) {
        return customerService.changeOrderStatusAfterStarted(orderId);
    }

    @PutMapping("/payFromCredit/{orderId}/{expertId}/{amount}")
    @ResponseBody
    public void payFromCredit(@PathVariable Long orderId, @PathVariable Long expertId, @PathVariable Long amount, Authentication authentication) {
        Customer authenticatedCustomer = (Customer) authentication.getPrincipal();
        customerService.payFromCredit(orderId, authenticatedCustomer.getId(), expertId, amount);
    }

    @GetMapping("/onlinePay")
    private String onlinePay() {
        return "payment";
    }

    @PostMapping("/onlinePay")
    @ResponseBody
    private String pay(@RequestParam("g-recaptcha-response") String captcha) {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=6LcKj7gjAAAAAAYY0U0irX6rLHFUuETgBmxZtjO1&response=" + captcha;
        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url + params, HttpMethod.POST, null, ReCaptchaResponse.class).getBody();
        if (reCaptchaResponse.isSuccess()) {
            return "done";
        }
        return "invalid";
    }

    @GetMapping("/viewOrderHistory")
    @ResponseBody
    public List<OrderDTO> viewOrderHistory(Authentication authentication) {
        Customer authenticatedCustomer = (Customer) authentication.getPrincipal();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        customerService.viewOrderHistory(authenticatedCustomer.getId())
                .forEach(
                        order -> orderDTOS.add(
                                mapper.map(order, OrderDTO.class))
        );
        return orderDTOS;
    }

    @PostMapping("/viewOrderHistory")
    @ResponseBody
    public List<OrderDTO> viewOrderHistory(@RequestBody CustomerOrderStatusDTO customerOrderStatusDTO, Authentication authentication) {
        Customer authenticatedCustomer = (Customer) authentication.getPrincipal();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        customerService
                .viewOrderHistory(
                        authenticatedCustomer.getId(),
                        customerOrderStatusDTO.getOrderStatus())
                .forEach(order -> orderDTOS.add(mapper.map(order, OrderDTO.class)));
        return orderDTOS;
    }

    @GetMapping("/viewCredit")
    public Long viewCredit(Authentication authentication) {
        Customer authenticatedCustomer = (Customer) authentication.getPrincipal();
        return customerService.viewCredit(authenticatedCustomer.getId());
    }
}
