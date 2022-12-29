package ir.maktab.homeserviceprovider.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderExpertCommentDTO {

    private Long orderId;
    private Long expertId;
    private Integer score;
    private String comment;
}
