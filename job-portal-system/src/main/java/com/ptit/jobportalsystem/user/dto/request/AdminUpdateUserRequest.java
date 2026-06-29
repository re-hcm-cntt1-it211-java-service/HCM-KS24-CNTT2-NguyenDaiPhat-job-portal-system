package com.ptit.jobportalsystem.user.dto.request;

import com.ptit.jobportalsystem.exception.UserErrorCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminUpdateUserRequest {

    @NotBlank(message = "FULL_NAME_REQUIRED")
    @Size(min = 2, max = 100,
            message = "FULL_NAME_INVALID_LENGTH")
    private String fullName;

    @NotBlank
    private String role;

    private Boolean isActive;
}
