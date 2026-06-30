package com.ptit.jobportalsystem.job.dto.request;

import com.ptit.jobportalsystem.job.entity.JobStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateJobStatusRequest {

    @NotNull
    private JobStatus status;
}