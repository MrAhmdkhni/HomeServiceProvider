package ir.maktab.homeserviceprovider.entity.comment;

import ir.maktab.homeserviceprovider.base.entity.BaseEntity;
import ir.maktab.homeserviceprovider.entity.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity<Long> {

    private Integer score;
    private String comment;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Comment(Integer score, String comment) {
        this.score = score;
        this.comment = comment;
    }
}
