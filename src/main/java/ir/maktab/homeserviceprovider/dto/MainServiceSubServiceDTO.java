package ir.maktab.homeserviceprovider.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainServiceSubServiceDTO {

    private Long mainServiceId;
    private String name;
    private Long basePrice;
    private String description;
}
