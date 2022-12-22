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
    public List<Offer> findByOrderId(Long orderId) {
        try {
            return repository.findByOrderId(orderId);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public int editIsAccept(Long offerId, Boolean isAccept) {
        return repository.editIsAccept(offerId, isAccept);
    }
}
