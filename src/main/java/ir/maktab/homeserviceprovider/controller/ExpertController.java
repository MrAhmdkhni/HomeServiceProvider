package ir.maktab.homeserviceprovider.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.maktab.homeserviceprovider.dto.OrderExpertOfferDTO;
import ir.maktab.homeserviceprovider.dto.PasswordDTO;
import ir.maktab.homeserviceprovider.dto.PersonDTO;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.exception.FileNotFoundException;
import ir.maktab.homeserviceprovider.service.ExpertService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/expert")
public class ExpertController {

    private final ExpertService expertService;

    public ExpertController(ExpertService expertService) {
        this.expertService = expertService;
    }


    @PostMapping("/signup")
    void signUp(@RequestParam("expert") String expert, @RequestParam("image") MultipartFile multipartFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PersonDTO personDTO = objectMapper.readValue(expert, PersonDTO.class);
            expertService.signUp(Expert.builder()
                            .firstname(personDTO.getFirstname())
                            .lastname(personDTO.getLastname())
                            .phoneNumber(personDTO.getPhoneNumber())
                            .email(personDTO.getEmail())
                            .password(personDTO.getPassword())
                            .image(multipartFile.getBytes())
                            .build(),
                    multipartFile.getOriginalFilename(), multipartFile.getSize());
        } catch (IOException e) {
            throw new FileNotFoundException("this file not found!");
        }
    }

    @PutMapping("/editPassword")
    public int editPassword(@RequestBody PasswordDTO passwordDTO) {
        return expertService.editPassword(passwordDTO.getId(), passwordDTO.getPassword());
    }

    @PostMapping("/addOfferForOrder")
    public void addOfferForOrder(@RequestBody OrderExpertOfferDTO orderExpertOfferDTO) {
        expertService.addOfferForOrder(orderExpertOfferDTO.getExpertId(), orderExpertOfferDTO.getOrderId(),
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

    @GetMapping("/viewExpertScore/{expertId}")
    public int viewExpertScore(@PathVariable Long expertId) {
        return expertService.viewExpertScore(expertId);
    }

    @GetMapping("/viewExpertScoreFromCustomerComment/{commentId}")
    public int viewExpertScoreFromCustomerComment(@PathVariable Long commentId) {
        return expertService.viewExpertScoreFromCustomerComment(commentId);
    }
}
