package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);
}
