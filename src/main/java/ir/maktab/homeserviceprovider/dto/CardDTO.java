package ir.maktab.homeserviceprovider.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDTO {

    @Pattern(
            regexp = "(?<!\\d)\\d{16}(?!\\d)|(?<!\\d[ _-])(?<!\\d)\\d{4}(?=([_ -]))(?:\\1\\d{4}){3}(?![_ -]?\\d)",
            message = "the format of the card-number is incorrect!")
    private String cardNumber;

    @Pattern(regexp = "^[0-9]{3,4}$", message = "the format of the cvv2 is incorrect!")
    private String cvv2;

    @Pattern(regexp = "^\\d{2}$", message = "the format of the number is incorrect!")
    private String month;

    @Pattern(regexp = "^\\d{2}$", message = "the format of the number is incorrect!")
    private String year;
}
