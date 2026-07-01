package com.ptit.jobportalsystem.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplyJobRequest {

    @NotNull(message = "APPLICATION_JOB_REQUIRED")
    private Long jobId;

    @NotNull(message = "APPLICATION_CV_REQUIRED")
    private Long cvId;

    @Size(max = 2000, message = "APPLICATION_COVER_LETTER_INVALID_LENGTH")
    private String coverLetter;
}
