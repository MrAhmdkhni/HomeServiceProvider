package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderDTO {

    private LocalDateTime creationDate;
    private Long proposedPrice;
    private String description;
    private String subServiceName;
    private OrderStatus orderStatus;
}
