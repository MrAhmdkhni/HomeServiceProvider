package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;

import java.io.File;
import java.util.Optional;

public interface ExpertService extends BaseService<Expert, Long> {

    Optional<Expert> findByEmail(String email);

    Optional<Expert> findByPhoneNumber(String phoneNumber);

    int editPassword(Long expertId, String newPassword);

    int editExpertStatus(Long expertId, ExpertStatus expertStatus);

    void signUp(Expert expert, File image);

    void addOfferForOrder(Long expertId, Long orderId, Offer offer);
}
