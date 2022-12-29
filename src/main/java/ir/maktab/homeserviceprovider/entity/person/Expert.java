package ir.maktab.homeserviceprovider.entity.person;

import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Expert extends Person {

    @Lob
    private byte[] image;
    private Integer score;
    @Enumerated(value = EnumType.STRING)
    private ExpertStatus expertStatus;
    private Long credit;
    private Boolean isActive;
    @ManyToMany(mappedBy = "experts", cascade = CascadeType.ALL)
    private Set<SubService> subServices = new HashSet<>();
    @OneToMany(mappedBy = "expert")
    private List<Offer> offers = new ArrayList<>();


    public Expert(String firstname, String lastname, String phoneNumber, String email, String password) {
        super(firstname, lastname, phoneNumber, email, password);
    }

    @Builder
    public Expert(String firstname, String lastname, String phoneNumber, String email, String password, byte[] image) {
        super(firstname, lastname, phoneNumber, email, password);
        this.image = image;
    }

    public void addSubService(SubService subService) {
        this.subServices.add(subService);
        subService.getExperts().add(this);
    }

    public void deleteSubService(SubService subService) {
        this.subServices.remove(subService);
        subService.getExperts().remove(this);
    }

    @Override
    public String toString() {
        return "Expert {" +
                "firstname = '" + getFirstname() + '\'' +
                ", lastname = '" + getLastname() + '\'' +
                ", phoneNumber = '" + getPhoneNumber() + '\'' +
                ", email = '" + getEmail() + '\'' +
                ", score = " + score +
                ", expertStatus = '" + expertStatus + '\'' +
                ", credit = " + credit +
                "} ";
    }
}
