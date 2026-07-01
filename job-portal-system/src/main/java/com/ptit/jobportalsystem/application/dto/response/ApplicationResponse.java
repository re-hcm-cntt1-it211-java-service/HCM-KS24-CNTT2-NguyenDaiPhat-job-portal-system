package com.ptit.jobportalsystem.application.dto.response;

import com.ptit.jobportalsystem.application.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationResponse {

    private Long id;

    private Long candidateId;

    private String candidateName;

    private Long jobId;

    private String jobTitle;

    private Long cvId;

    private String cvFileName;

    private String cvFileUrl;

    private String coverLetter;

    private ApplicationStatus status;
}