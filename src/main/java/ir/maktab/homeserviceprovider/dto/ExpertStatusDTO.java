package ir.maktab.homeserviceprovider.dto;

import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpertStatusDTO {

    private Long expertId;
    private ExpertStatus expertStatus;
}
