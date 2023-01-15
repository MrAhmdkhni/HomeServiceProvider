package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpertDTO {

    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private Integer score;
    private ExpertStatus expertStatus;
    private Long credit;
    private Boolean isActive;
}
