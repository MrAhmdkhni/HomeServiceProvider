package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.offer.TimeType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderExpertOfferDTO {

    private Long expertId;
    private Long orderId;
    private String offer;
    private Long proposedPrice;
    private TimeType timeType;
    private Integer durationTime;
    private String  endDate;
    private String endTime;
}
