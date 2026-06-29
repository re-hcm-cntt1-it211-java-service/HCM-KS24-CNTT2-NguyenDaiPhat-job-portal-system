package com.ptit.jobportalsystem.security.jwt;

import com.ptit.jobportalsystem.security.principal.UserPrincipal;
import com.ptit.jobportalsystem.user.entity.User;

public record TokenClaims(
        Long userId,
        String email,
        String role
) {
    // Factory method — tạo từ UserPrincipal lúc login
    public static TokenClaims from(UserPrincipal principal) {
        return new TokenClaims(
                principal.getId(),
                principal.getEmail(),
                principal.getAuthorities().iterator().next().getAuthority()
        );
    }

    // Factory method — tạo từ User entity lúc refresh
    public static TokenClaims from(User user) {
        return new TokenClaims(
                user.getId(),
                user.getEmail(),
                user.getRole().getName()
        );
    }
}
