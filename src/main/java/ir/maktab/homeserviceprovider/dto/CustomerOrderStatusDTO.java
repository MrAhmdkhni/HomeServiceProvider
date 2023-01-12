package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrderStatusDTO {

    private Long customerId;
    private OrderStatus orderStatus;
}
