package ir.maktab.homeserviceprovider.entity.offer;

import ir.maktab.homeserviceprovider.base.entity.BaseEntity;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Offer extends BaseEntity<Long> {

    private String offer;
    @Column(name = "proposed-price")
    private Long proposedPrice;
    @Column(name = "time_type")
    @Enumerated(value = EnumType.STRING)
    private TimeType timeType;
    @Column(name = "duration-time")
    private Integer durationTime;
    @Column(name = "is-accept")
    private Boolean isAccept;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Expert expert;

    public Offer(String offer, Long proposedPrice, TimeType timeType, Integer durationTime) {
        this.offer = offer;
        this.proposedPrice = proposedPrice;
        this.timeType = timeType;
        this.durationTime = durationTime;
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
