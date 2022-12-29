package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExpertDTO {

    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;
    private Integer score;
    private ExpertStatus expertStatus;
    private Long credit;
    private Boolean isActive;
}
