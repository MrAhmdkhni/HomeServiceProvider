package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.offer.TimeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {

    private Long proposedPrice;
    private TimeType timeType;
    private Integer durationTime;
    private Integer expertScore;
    private LocalDateTime endTime;
}
