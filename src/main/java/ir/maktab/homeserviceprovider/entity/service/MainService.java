package ir.maktab.homeserviceprovider.entity.service;

import ir.maktab.homeserviceprovider.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "main_service")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class MainService extends BaseEntity<Long> {

    @Column(nullable = false)
    private String name;
    @ToString.Exclude
    @OneToMany(mappedBy = "mainService")
    private List<SubService> subServices = new ArrayList<>();

    public MainService(String name) {
        this.name = name;
    }
}
