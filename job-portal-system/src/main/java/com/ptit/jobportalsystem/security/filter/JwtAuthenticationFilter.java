package com.ptit.jobportalsystem.security.filter;

import com.nimbusds.jwt.JWTClaimsSet;
import com.ptit.jobportalsystem.auth.service.TokenService;
import com.ptit.jobportalsystem.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (StringUtils.hasText(token)) {
            // Thêm check blacklist — nếu đã logout thì reject
            if (tokenService.isBlacklisted(token)) {
                filterChain.doFilter(request, response);
                return; // không set SecurityContext → 401
            }

            try {
                JWTClaimsSet claims = jwtTokenProvider.validateAndExtractClaims(token);

                String userId = claims.getStringClaim("userId");
                String role = claims.getStringClaim("role");

                var auth = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        List.of(new SimpleGrantedAuthority(role))
                );

                SecurityContextHolder.getContext().setAuthentication(auth);


            } catch (Exception e) {
                log.warn("Could not authenticate request: {}", e.getMessage());
                // Không set authentication → Spring Security sẽ trả 401 tự động
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer"))
            return bearer.substring(7);

        return null;
    }
}

