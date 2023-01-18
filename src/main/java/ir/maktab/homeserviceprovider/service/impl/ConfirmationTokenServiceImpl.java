package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.entity.token.ConfirmationToken;
import ir.maktab.homeserviceprovider.repository.ConfirmationTokenRepository;
import ir.maktab.homeserviceprovider.service.ConfirmationTokenService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfirmationTokenServiceImpl
        extends BaseServiceImpl<ConfirmationToken, Long, ConfirmationTokenRepository>
        implements ConfirmationTokenService {

    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository repository) {
        super(repository);
    }


    @Override
    public Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken) {
        try {
            return repository.findByConfirmationToken(confirmationToken);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}