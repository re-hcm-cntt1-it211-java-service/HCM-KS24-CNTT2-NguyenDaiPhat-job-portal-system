package com.ptit.jobportalsystem.job.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateJobRequest {

    @NotBlank(message = "JOB_TITLE_REQUIRED")
    @Size(max = 255, message = "JOB_TITLE_INVALID_LENGTH")
    private String title;

    @NotBlank(message = "JOB_DESCRIPTION_REQUIRED")
    private String description;

    @DecimalMin(value = "0", message = "JOB_SALARY_INVALID")
    private BigDecimal salary;

    @NotBlank(message = "JOB_LOCATION_REQUIRED")
    @Size(max = 255, message = "JOB_LOCATION_INVALID_LENGTH")
    private String location;

    @NotNull(message = "JOB_DEADLINE_REQUIRED")
    @Future(message = "JOB_DEADLINE_INVALID")
    private LocalDateTime deadline;
}