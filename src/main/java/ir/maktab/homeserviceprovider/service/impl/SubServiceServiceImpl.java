package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.repository.SubServiceRepository;
import ir.maktab.homeserviceprovider.service.SubServiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SubServiceServiceImpl
        extends BaseServiceImpl<SubService, Long, SubServiceRepository>
        implements SubServiceService {


    public SubServiceServiceImpl(SubServiceRepository repository) {
        super(repository);
    }

    @Override
    public Optional<SubService> findByName(String name) {
        try {
            return repository.findByName(name);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public int deleteSubServiceById(Long subServiceId) {
        return repository.deleteSubServiceById(subServiceId);
    }

    @Override
    @Transactional
    public int editBasePriceAndDescription(Long subServiceId, Long basePrice, String description) {
        return repository.editBasePriceAndDescription(subServiceId, basePrice, description);
    }
}
