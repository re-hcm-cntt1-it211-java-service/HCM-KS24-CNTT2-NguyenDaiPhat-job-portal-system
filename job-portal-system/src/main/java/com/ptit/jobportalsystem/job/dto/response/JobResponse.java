package com.ptit.jobportalsystem.job.dto.response;

import com.ptit.jobportalsystem.job.entity.JobStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class JobResponse {

    private Long id;

    private Long employerId;

    private String employerName;

    private String title;

    private String description;

    private BigDecimal salary;

    private String location;

    private JobStatus status;

    private LocalDateTime deadline;
}