package com.ptit.jobportalsystem.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateProfileRequest {

    @NotBlank(message = "FULL_NAME_REQUIRED")
    @Size(min = 2, max = 100, message = "FULL_NAME_INVALID_LENGTH")
    private String fullName;

}
