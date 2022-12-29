package ir.maktab.homeserviceprovider.entity.service;

import ir.maktab.homeserviceprovider.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@Builder
public class MainService extends BaseEntity<Long> {

    @Column(nullable = false)
    private String name;
    @ToString.Exclude
    @OneToMany(mappedBy = "mainService")
    private List<SubService> subServices = new ArrayList<>();

    public MainService(String name) {
        this.name = name;
    }

    public MainService(String name, List<SubService> subServices) {
        this.name = name;
        this.subServices = subServices;
    }
}
