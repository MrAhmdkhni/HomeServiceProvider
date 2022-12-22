package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.repository.MainServiceRepository;
import ir.maktab.homeserviceprovider.service.MainServiceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MainServiceServiceImpl
        extends BaseServiceImpl<MainService, Long, MainServiceRepository>
        implements MainServiceService {

    public MainServiceServiceImpl(MainServiceRepository repository) {
        super(repository);
    }

    @Override
    public Optional<MainService> findByName(String name) {
        try {
            return repository.findByName(name);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public int deleteByName(String name) {
        return repository.deleteByName(name);
    }
}
