package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExpertFilterDTO {

    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private Boolean isActive;
    private ExpertStatus expertStatus;

    private Integer maxScore;
    private Integer minScore;
    private Long maxCredit;
    private Long minCredit;
    private String maxCreationDate;
    private String minCreationDate;
}
