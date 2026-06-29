package com.ptit.jobportalsystem.user.dto.request;

import com.ptit.jobportalsystem.exception.UserErrorCode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminCreateUserRequest {

    @NotBlank(message = "FULL_NAME_REQUIRED")
    @Size(min = 2, max = 100,
            message = "FULL_NAME_INVALID_LENGTH")
    private String fullName;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID_FORMAT")
    @Size(max = 100,
            message = "EMAIL_TOO_LONG")
    private String email;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, max = 100,
            message = "PASSWORD_INVALID_LENGTH")
    private String password;

    @NotBlank
    private String role;
}