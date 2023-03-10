package ir.maktab.homeserviceprovider.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubServiceDTO {

    private String subServiceName;
    private Long basePrice;
    private String description;
    private String mainServiceName;
}
