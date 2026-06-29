package com.ptit.jobportalsystem.auth.service;

import com.ptit.jobportalsystem.auth.dto.response.TokenPair;
import com.ptit.jobportalsystem.auth.enity.RefreshToken;
import com.ptit.jobportalsystem.auth.repository.RefreshTokenRepository;
import com.ptit.jobportalsystem.security.jwt.JwtTokenProvider;
import com.ptit.jobportalsystem.security.jwt.TokenClaims;
import com.ptit.jobportalsystem.security.principal.UserPrincipal;
import com.ptit.jobportalsystem.user.entity.User;
import com.ptit.jobportalsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${app.jwt.access-token-expiration}")
    private long accessTokenExpirationMs;

    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpirationMs;

    // Login xong gọi cái này
    public TokenPair issueTokenPair(UserPrincipal principal) {
        TokenClaims claims = TokenClaims.from(principal);
        return issue(claims, principal.getId());
    }

    // Đổi refresh token lấy cặp mới
    public TokenPair refresh(String rawRefreshToken) {
        RefreshToken stored = refreshTokenRepository
                .findByTokenAndRevokedFalse(rawRefreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (stored.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        // Rotate — revoke token cũ trước
        stored.setRevoked(true);
        refreshTokenRepository.save(stored);

        // Tạo claims từ User entity — không cần UserPrincipal
        User user = stored.getUser();
        TokenClaims claims = TokenClaims.from(user);
        return issue(claims, user.getId());
    }

    public void logout(String accessToken, String rawRefreshToken) {
        // Blacklist access token trong Redis
        long remainingMs = jwtTokenProvider.getRemainingMs(accessToken);
        if (remainingMs > 0) {
            redisTemplate.opsForValue().set(
                    blacklistKey(accessToken),
                    "revoked",
                    Duration.ofMillis(remainingMs)
            );
        }

        // Revoke refresh token
        refreshTokenRepository.findByToken(rawRefreshToken)
                .ifPresent(rt -> {
                    rt.setRevoked(true);
                    refreshTokenRepository.save(rt);
                });
    }

    public boolean isBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey(accessToken)));
    }

    // Private — dùng chung cho issueTokenPair và refresh
    private TokenPair issue(TokenClaims claims, Long userId) {
        String accessToken  = jwtTokenProvider.generateToken(claims, accessTokenExpirationMs);
        String refreshToken = createRefreshToken(userId);
        return new TokenPair(accessToken, refreshToken);
    }

    private String createRefreshToken(Long userId) {
        String token = UUID.randomUUID().toString();

        RefreshToken entity = RefreshToken.builder()
                .token(token)
                .user(userRepository.getReferenceById(userId))
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .revoked(false)
                .build();

        refreshTokenRepository.save(entity);
        return token;
    }

    private String blacklistKey(String token) {
        return "blacklist:" + token;
    }
}
