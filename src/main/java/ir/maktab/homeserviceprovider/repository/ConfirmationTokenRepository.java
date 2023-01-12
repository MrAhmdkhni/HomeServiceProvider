package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    Optional<ConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("update ConfirmationToken ct set ct.confirmedAt = ?2 where ct.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime localDateTime);
}
