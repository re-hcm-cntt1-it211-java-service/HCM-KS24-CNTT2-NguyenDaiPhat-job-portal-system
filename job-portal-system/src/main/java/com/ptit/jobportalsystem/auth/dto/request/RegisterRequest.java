package com.ptit.jobportalsystem.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @NotBlank(message = "FULL_NAME_REQUIRED")
    @Size(min = 2, max = 100, message = "FULL_NAME_INVALID_LENGTH")
    private String fullName;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID_FORMAT")
    @Size(max = 255, message = "EMAIL_TOO_LONG")
    private String email;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, max = 100, message = "PASSWORD_INVALID_LENGTH")
//    @Pattern(
//            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
//            message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit"
//    )
    private String password;
}
