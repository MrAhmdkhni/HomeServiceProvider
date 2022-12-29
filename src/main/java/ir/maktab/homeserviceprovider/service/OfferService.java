package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.entity.offer.Offer;

import java.util.List;

public interface OfferService extends BaseService<Offer, Long> {

    List<Offer> findByOrderIdBasedOnProposedPrice(Long orderId);

    List<Offer> findByOrderIdBasedOnExpertScore(Long orderId);

    int editIsAccept(Long offerId, Boolean isAccept);
}
