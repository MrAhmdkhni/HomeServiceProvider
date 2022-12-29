package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerFilterDTO {

    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;
    private Long maxCredit;
    private Long minCredit;
}
