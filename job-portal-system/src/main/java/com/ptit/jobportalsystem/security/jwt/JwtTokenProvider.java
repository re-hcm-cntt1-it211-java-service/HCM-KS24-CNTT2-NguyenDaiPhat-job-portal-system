package com.ptit.jobportalsystem.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.ptit.jobportalsystem.exception.AppException;
import com.ptit.jobportalsystem.exception.SecurityErrorCode;
import com.ptit.jobportalsystem.security.principal.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    private final SecretKey secretKey;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret) {
        byte [] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(TokenClaims claims, long expirationMs) {
        try {
            Instant now = Instant.now();
            Instant expiry = now.plusMillis(expirationMs);

            JWTClaimsSet jwtClaims = new JWTClaimsSet.Builder()
                    .subject(claims.email()) // return ra email
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expiry))
                    // Custom claims — nhúng context vào token để filter đọc
                    .claim("userId", claims.userId())
                    .claim("role", claims.role())
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaims);
            signedJWT.sign(new MACSigner(secretKey));

            return signedJWT.serialize();

        } catch (JOSEException e) {
            log.error("Không thể tạo JWT token", e);
            throw new AppException(SecurityErrorCode.JWT_GENERATION_FAILED);
        }
    }

    public JWTClaimsSet validateAndExtractClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey);

            if (!signedJWT.verify(verifier))
                throw new AppException(SecurityErrorCode.ACCESS_TOKEN_INVALID);

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            if (claims.getExpirationTime().before(new Date()))
                throw new AppException(SecurityErrorCode.ACCESS_TOKEN_EXPIRED);

            return claims;

        } catch (AppException e) {
            throw e;
        }
        catch (ParseException e) {
            log.warn("JWT parse thất bại", e);
            throw new AppException(SecurityErrorCode.ACCESS_TOKEN_INVALID);
        }
        catch (JOSEException e) {
            log.warn("Xác thực chữ ký JWT thất bại", e);
            throw new AppException(SecurityErrorCode.ACCESS_TOKEN_INVALID);
        }

    }

    public long getRemainingMs(String token) {
        JWTClaimsSet claims = validateAndExtractClaims(token);

        long expiration = claims.getExpirationTime().getTime();
        long now = System.currentTimeMillis();

        return Math.max(expiration - now, 0);
    }
}

