package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.dto.ExpertFilterDTO;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;

import java.util.List;
import java.util.Optional;

public interface ExpertService extends BaseService<Expert, Long> {

    Optional<Expert> findByEmail(String email);

    Optional<Expert> findByUsername(String phoneNumber);

    int editPassword(Long expertId, String newPassword);

    int changeExpertStatus(Long expertId, ExpertStatus expertStatus);

    void signUp(Expert expert, String imageName, Long imageSize);

    String signUpWithValidation(Expert expert, String imageName, Long imageSize);

    int activeExpert(String email, ExpertStatus expertStatus);

    List<Order> viewOrdersRelatedToExpert(Long expertId);

    void addOfferForOrder(Long expertId, Long orderId, Offer offer);//name must be change!

    int changeExpertActivation(Long expertId, Boolean isActive);

    int updateScore(Long expertId, Integer newScore);

    int viewExpertScore(Long expertId);

    int viewExpertScoreFromCustomerComment(Long commentId);

    void updateCredit(Long expertId, Long newCredit);

    List<Expert> expertsFilter(ExpertFilterDTO expertDTO);

    List<Offer> viewOfferHistory(Long expertId, boolean isAccept);

    List<Order> viewOrderHistory(Long expertId, boolean isAccept);

    List<Order> viewOrderHistory(Long expertId, boolean isAccept, OrderStatus orderStatus);

    Long viewCredit(Long expertId);


}
