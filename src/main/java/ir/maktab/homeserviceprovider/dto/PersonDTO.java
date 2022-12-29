package ir.maktab.homeserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PersonDTO {

    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String email;
    private String password;
}
