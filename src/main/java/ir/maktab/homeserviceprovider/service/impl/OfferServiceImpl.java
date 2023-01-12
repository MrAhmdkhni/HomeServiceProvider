package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.repository.OfferRepository;
import ir.maktab.homeserviceprovider.service.OfferService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class OfferServiceImpl
        extends BaseServiceImpl<Offer, Long, OfferRepository> implements OfferService {

    public OfferServiceImpl(OfferRepository repository) {
        super(repository);
    }


    @Override
    @Transactional
    public List<Offer> findByOrderIdBasedOnProposedPrice(Long orderId) {
        try {
            return repository.findByOrderIdBasedOnProposedPrice(orderId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


    @Override
    public List<Offer> findByOrderIdBasedOnExpertScore(Long orderId) {
        try {
            return repository.findByOrderIdBasedOnExpertScore(orderId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public int editIsAccept(Long offerId, Boolean isAccept) {
        return repository.editIsAccept(offerId, isAccept);
    }

    @Override
    @Transactional
    public List<Offer> findOfferByExpertIdAndIsAccept(Long expertId, boolean isAccept) {
        try {
            return repository.findOfferByExpertIdAndIsAccept(expertId, isAccept);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public int calculateNumberOfRegisteredOffers(Long expertId) {
        return repository.calculateNumberOfRegisteredOffers(expertId);
    }

    @Override
    public int calculateNumberOfRegisteredOffers(Long expertId, Boolean isAccept) {
        return repository.calculateNumberOfRegisteredOffers(expertId, isAccept);
    }
}
