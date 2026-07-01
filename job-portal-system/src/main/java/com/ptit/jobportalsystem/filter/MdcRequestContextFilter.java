package com.ptit.jobportalsystem.filter;

import com.ptit.jobportalsystem.security.principal.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class MdcRequestContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String traceId = UUID.randomUUID().toString();

            MDC.put("traceID", traceId);

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null
                    && authentication.isAuthenticated()
                    && !(authentication instanceof AnonymousAuthenticationToken)
                    && authentication.getPrincipal() instanceof UserPrincipal principal) {

                MDC.put("userId", String.valueOf(principal.getId()));
                MDC.put("email", principal.getEmail());
                MDC.put("role", principal.getRole());

            } else {

                MDC.put("userId", "ANONYMOUS");
                MDC.put("email", "-");
                MDC.put("role", "ANONYMOUS");
            }

            filterChain.doFilter(request, response);

        } finally {
            MDC.clear();
        }

    }
}
