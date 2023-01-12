package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpertOrderHistoryDTO {

    private Long expertId;
    private Boolean isAccept;
    private OrderStatus orderStatus;
}
