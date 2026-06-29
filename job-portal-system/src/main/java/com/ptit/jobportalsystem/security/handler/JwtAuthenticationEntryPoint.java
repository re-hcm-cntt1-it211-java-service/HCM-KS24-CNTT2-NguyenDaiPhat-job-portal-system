package com.ptit.jobportalsystem.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptit.jobportalsystem.common.response.ApiResponse;
import com.ptit.jobportalsystem.exception.ErrorCode;
import com.ptit.jobportalsystem.exception.SecurityErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
//@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        ErrorCode error = SecurityErrorCode.UNAUTHORIZED;

        response.setStatus(error.getHttpStatus().value());
        response.setContentType("application/json");

        objectMapper.writeValue(
                response.getOutputStream(),
                ApiResponse.builder()
                        .code(error.getCode())
                        .message(error.getMessage())
                        .build()
        );
    }
}

