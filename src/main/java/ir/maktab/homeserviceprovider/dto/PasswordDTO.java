package ir.maktab.homeserviceprovider.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDTO {

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8}$",
            message = "the password must contain numbers, lowercase and uppercase letters")
    String password;
}
