package com.ptit.jobportalsystem.application.service;

import com.ptit.jobportalsystem.application.dto.request.ApplyJobRequest;
import com.ptit.jobportalsystem.application.dto.response.ApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandidateApplicationService {

    ApplicationResponse applyJob(ApplyJobRequest request);

    Page<ApplicationResponse> getMyApplications(Pageable pageable);
}