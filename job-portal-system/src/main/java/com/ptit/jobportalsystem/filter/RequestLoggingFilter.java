package com.ptit.jobportalsystem.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        long start = System.currentTimeMillis();

        try {
            log.info("HTTP_REQUEST method={} path={}", request.getMethod(), request.getRequestURI());

            filterChain.doFilter(request, response);

        } finally {
//            log.info("HTTP_RESPONSE status{} ducation{}ms", response.getStatus(), ducation);
            long duration = System.currentTimeMillis() - start;

            log.info(
                    "HTTP_RESPONSE status={} duration={}ms",
                    response.getStatus(),
                    duration
            );
        }

    }
}
