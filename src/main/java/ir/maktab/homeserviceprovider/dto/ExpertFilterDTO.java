package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ExpertFilterDTO {

    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;
    private Integer maxScore;
    private Integer minScore;
    private ExpertStatus expertStatus;
    private Long maxCredit;
    private Long minCredit;
    private Boolean isActive;
}
