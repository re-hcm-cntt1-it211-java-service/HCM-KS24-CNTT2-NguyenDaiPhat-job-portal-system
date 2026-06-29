package com.ptit.jobportalsystem.auth.repository;

import com.ptit.jobportalsystem.auth.enity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenAndRevokedFalse(String rawRefreshToken);

    Optional<RefreshToken> findByToken(String rawRefreshToken);
}
