package ir.maktab.homeserviceprovider.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    private String firstname;
    private String lastname;
    @Email(message = "the format of the email is incorrect!")
    private String email;
    private String username;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8}$",
            message = "the password must contain numbers, lowercase and uppercase letters")
    private String password;
}
