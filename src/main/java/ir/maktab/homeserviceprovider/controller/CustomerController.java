package ir.maktab.homeserviceprovider.controller;

import ir.maktab.homeserviceprovider.dto.*;
import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.service.CustomerService;
import ir.maktab.homeserviceprovider.util.ReCaptchaResponse;
import org.springframework.http.HttpMethod;
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

    public CustomerController(CustomerService customerService, RestTemplate restTemplate) {
        this.customerService = customerService;
        this.restTemplate = restTemplate;
    }


    @GetMapping("/findByEmail/{email}")
    @ResponseBody
    public PersonDTO findByEmail(@PathVariable String email){
        Customer customer = customerService.findByEmail(email).get();
         return new PersonDTO(
                 customer.getFirstname(),
                 customer.getLastname(),
                 customer.getPhoneNumber(),
                 customer.getEmail(),
                 customer.getPassword());
    }

    @GetMapping("/findByPhoneNumber/{phoneNumber}")
    @ResponseBody
    public PersonDTO findByPhoneNumber(@PathVariable String phoneNumber) {
        Customer customer = customerService.findByPhoneNumber(phoneNumber).get();
        return new PersonDTO(
                customer.getFirstname(),
                customer.getLastname(),
                customer.getPhoneNumber(),
                customer.getEmail(),
                customer.getPassword());
    }

    @GetMapping("/existsByEmail/{email}")
    @ResponseBody
    public boolean existsByEmail(@PathVariable String email) {
        return customerService.existsByEmail(email);
    }

    @PostMapping("/signup")
    @ResponseBody
    public void singUp(@RequestBody PersonDTO personDTO) {
        customerService.signUp(
                Customer.builder()
                        .firstname(personDTO.getFirstname())
                        .lastname(personDTO.getLastname())
                        .phoneNumber(personDTO.getPhoneNumber())
                        .email(personDTO.getEmail())
                        .password(personDTO.getPassword())
                        .build());
    }

    @PutMapping("/editPassword")
    @ResponseBody
    public int editPassword(@RequestBody PasswordDTO passwordDTO) {
        return customerService.editPassword(passwordDTO.getId(), passwordDTO.getPassword());
    }

    @GetMapping("/findAllMainService")
    @ResponseBody
    public List<MainServiceDTO> findAllMainService() {
        List<MainServiceDTO> mainServiceDTOS = new ArrayList<>();
        customerService.findAllMainService().forEach(mainService -> {
            mainServiceDTOS.add(new MainServiceDTO(mainService.getName()));
        });
        return mainServiceDTOS;
    }

    @GetMapping("/findAllSubService")
    @ResponseBody
    public List<SubServiceDTO> findAllSubService() {
        List<SubServiceDTO> subServiceDTOS = new ArrayList<>();
        customerService.findAllSubService().forEach(subService -> {
            subServiceDTOS.add(new SubServiceDTO(
                    subService.getName(),
                    subService.getBasePrice(),
                    subService.getDescription(),
                    subService.getMainService().getName()));
        });
        return subServiceDTOS;
    }

    @PostMapping("/addOrder")
    @ResponseBody
    public void addOrder(@RequestBody CustomerSubServiceOrderDTO customerSubServiceOrderDTO) {
        customerService.addOrder(customerSubServiceOrderDTO.getCustomerId(), customerSubServiceOrderDTO.getSubServiceId(),
                Order.builder().proposedPrice(customerSubServiceOrderDTO.getProposedPrice())
                        .description(customerSubServiceOrderDTO.getDescription())
                        .executionTime(LocalDateTime.of(
                                        LocalDate.parse(customerSubServiceOrderDTO.getExecutionDate()),
                                        LocalTime.parse(customerSubServiceOrderDTO.getExecutionTime())))
                        .address(customerSubServiceOrderDTO.getAddress())
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

    @GetMapping("/viewCustomerOrders/{customerId}")
    @ResponseBody
    public List<OrderDTO> viewCustomerOrders(@PathVariable Long customerId){
        List<OrderDTO> orderDTOS = new ArrayList<>();
        customerService.viewCustomerOrders(customerId).forEach(order -> {
            orderDTOS.add(new OrderDTO(
                    order.getCreationDate(),
                    order.getProposedPrice(),
                    order.getDescription(),
                    order.getSubService().getName(),
                    order.getOrderStatus()));
        });
        return orderDTOS;
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

    @PutMapping("/payFromCredit/{orderId}/{customerId}/{expertId}/{amount}")
    @ResponseBody
    public void payFromCredit(@PathVariable Long orderId, @PathVariable Long customerId, @PathVariable Long expertId, @PathVariable Long amount) {
        customerService.payFromCredit(orderId, customerId, expertId, amount);
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
        ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url + params, HttpMethod.POST, null,ReCaptchaResponse.class).getBody();
        if (reCaptchaResponse.isSuccess()){
            return "done";
        }
        return "invalid";
    }
}
