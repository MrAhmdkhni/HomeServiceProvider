package ir.maktab.homeserviceprovider.entity.offer;

import ir.maktab.homeserviceprovider.base.entity.BaseEntity;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer extends BaseEntity<Long> {

    private String offer;
    private Long proposedPrice;
    @Enumerated(value = EnumType.STRING)
    private TimeType timeType;
    private Integer durationTime;
    private Boolean isAccept;
    private LocalDateTime endTime;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Expert expert;

    public Offer(String offer, Long proposedPrice, TimeType timeType, Integer durationTime, LocalDateTime endTime) {
        this.offer = offer;
        this.proposedPrice = proposedPrice;
        this.timeType = timeType;
        this.durationTime = durationTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Offer {" +
                "offer='" + offer + '\'' +
                ", proposedPrice=" + proposedPrice +
                ", timeType=" + timeType +
                ", durationTime=" + durationTime +
                ", isAccept=" + isAccept +
                ", expertScore='" + expert.getScore() + '\'' +
                ", creationDate=" + getCreationDate() +
                "} ";
    }
}
