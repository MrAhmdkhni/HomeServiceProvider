package ir.maktab.homeserviceprovider.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.maktab.homeserviceprovider.dto.*;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.exception.FileNotFoundException;
import ir.maktab.homeserviceprovider.service.ExpertService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/expert")
public class ExpertController {

    private final ExpertService expertService;
    private final ModelMapper mapper;

    public ExpertController(ExpertService expertService, ModelMapper mapper) {
        this.expertService = expertService;
        this.mapper = mapper;
    }


    @PostMapping("/signup")
    void signUp(@RequestParam("expert") String expert, @RequestParam("image") MultipartFile multipartFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PersonDTO personDTO = objectMapper.readValue(expert, PersonDTO.class);
            expertService.signUp(Expert.builder()
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

    @PostMapping("/editPassword")
    public int editPassword(@Valid @RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.editPassword(authenticatedExpert.getId(), passwordDTO.getPassword());
    }

    @GetMapping("/viewOrdersRelated")
    public List<OrderDTO> viewOrdersRelatedToExpert(Authentication authentication) {
        List<OrderDTO> orderDTOS = new ArrayList<>();
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        expertService.viewOrdersRelatedToExpert(authenticatedExpert.getId())
                .forEach(
                        order -> orderDTOS.add(
                                mapper.map(order, OrderDTO.class)));
        return orderDTOS;
    }

    @PostMapping("/addOfferForOrder")
    public void addOfferForOrder(@RequestBody OrderExpertOfferDTO orderExpertOfferDTO, Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        expertService.addOfferForOrder(authenticatedExpert.getId(), orderExpertOfferDTO.getOrderId(),
                Offer.builder()
                        .offer(orderExpertOfferDTO.getOffer())
                        .proposedPrice(orderExpertOfferDTO.getProposedPrice())
                        .timeType(orderExpertOfferDTO.getTimeType())
                        .durationTime(orderExpertOfferDTO.getDurationTime())
                        .endTime(LocalDateTime.of(
                                LocalDate.parse(orderExpertOfferDTO.getEndDate()),
                                LocalTime.parse(orderExpertOfferDTO.getEndTime())))
                        .build());
    }

    @GetMapping("/viewExpertScore")
    public int viewExpertScore(Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.viewExpertScore(authenticatedExpert.getId());
    }

    @GetMapping("/viewExpertScoreFromCustomerComment/{commentId}")
    public int viewExpertScoreFromCustomerComment(@PathVariable Long commentId) {
        return expertService.viewExpertScoreFromCustomerComment(commentId);
    }

    @GetMapping("/viewOfferHistory/{isAccept}")
    public List<OfferDTO> viewOfferHistory(@PathVariable boolean isAccept, Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        List<OfferDTO> offerDTOS = new ArrayList<>();
        expertService.viewOfferHistory(authenticatedExpert.getId(), isAccept)
                .forEach(
                        offer -> offerDTOS.add(
                                mapper.map(offer, OfferDTO.class)));
        return offerDTOS;
    }

    @GetMapping("/viewOrderHistory/{isAccept}")
    public List<OrderDTO> viewOrderHistory(@PathVariable boolean isAccept, Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        expertService.viewOrderHistory(authenticatedExpert.getId(), isAccept)
                .forEach(
                        order -> orderDTOS.add(
                                mapper.map(order, OrderDTO.class)));
        return orderDTOS;
    }

    @PostMapping("/viewOrderHistory")
    public List<OrderDTO> viewOrderHistory(@RequestBody ExpertOrderHistoryDTO expertOrderHistoryDTO, Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        expertService.viewOrderHistory(authenticatedExpert.getId(), expertOrderHistoryDTO.getIsAccept(), expertOrderHistoryDTO.getOrderStatus())
                .forEach(
                        order -> orderDTOS.add(
                                mapper.map(order, OrderDTO.class)));
        return orderDTOS;
    }

    @GetMapping("/viewCredit")
    public Long viewCredit(Authentication authentication) {
        Expert authenticatedExpert = (Expert) authentication.getPrincipal();
        return expertService.viewCredit(authenticatedExpert.getId());
    }
}
