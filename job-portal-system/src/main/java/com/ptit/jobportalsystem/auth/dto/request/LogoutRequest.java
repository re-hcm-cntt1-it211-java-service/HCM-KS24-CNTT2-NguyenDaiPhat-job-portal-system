package com.ptit.jobportalsystem.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LogoutRequest {

    @NotBlank(message = "REFRESH_TOKEN_REQUIRED")
    private String refreshToken;

}
