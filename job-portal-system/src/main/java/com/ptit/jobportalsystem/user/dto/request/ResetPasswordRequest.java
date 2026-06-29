package com.ptit.jobportalsystem.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ResetPasswordRequest {

    @NotBlank(message = "TOKEN_REQUIRED")
    private String token;

    @NotBlank(message = "NEW_PASSWORD_REQUIRED")
    @Size(min = 8,max = 100,
            message = "PASSWORD_INVALID_LENGTH")
    private String newPassword;

    @NotBlank(message = "CONFIRM_PASSWORD_REQUIRED")
    private String confirmPassword;

}