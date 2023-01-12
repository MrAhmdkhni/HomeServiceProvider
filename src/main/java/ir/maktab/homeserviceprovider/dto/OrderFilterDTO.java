package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFilterDTO {

    private String address;
    private String description;
    private OrderStatus orderStatus;
    private Long subServiceId;
    private String subServiceName;
    private String mainServiceName;

    private Long maxProposedPrice;
    private Long proposedPrice;
    private Long minProposedPrice;
    private String maxCreationDate;
    private /*String */LocalDateTime CreationDate;
    private String minCreationDate;
    private String maxUpdateTime;
    private /*String */LocalDateTime updateTime;
    private String minUpdateTime;

}
