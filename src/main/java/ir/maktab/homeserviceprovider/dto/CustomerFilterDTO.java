package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerFilterDTO {

    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private Boolean isActive;
    private Long maxCredit;
    private Long credit;
    private Long minCredit;
    private String maxCreationDate;
    private /*String*/ LocalDateTime CreationDate;
    private String minCreationDate;
}
