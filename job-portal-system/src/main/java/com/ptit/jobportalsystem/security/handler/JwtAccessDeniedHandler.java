package com.ptit.jobportalsystem.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ptit.jobportalsystem.common.response.ApiResponse;
import com.ptit.jobportalsystem.exception.ErrorCode;
import com.ptit.jobportalsystem.exception.SecurityErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
//@RequiredArgsConstructor
public class JwtAccessDeniedHandler
        implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception
    ) throws IOException {

        ErrorCode error = SecurityErrorCode.ACCESS_DENIED;

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
