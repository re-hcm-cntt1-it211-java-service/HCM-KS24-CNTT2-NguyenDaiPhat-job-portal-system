package com.ptit.jobportalsystem.application.dto.request;

import com.ptit.jobportalsystem.application.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateApplicationStatusRequest {

    @NotNull(message = "APPLICATION_STATUS_REQUIRED")
    private ApplicationStatus status;
}