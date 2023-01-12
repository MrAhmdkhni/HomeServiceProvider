package ir.maktab.homeserviceprovider.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubServiceOrderDTO {

    /*private Long customerId;*/
    private Long subServiceId;
    private Long proposedPrice;
    private String description;
    private String  executionDate;
    private String executionTime;
    private String address;
}
