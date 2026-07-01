package com.ptit.jobportalsystem.application.service;

import com.ptit.jobportalsystem.application.dto.request.UpdateApplicationStatusRequest;
import com.ptit.jobportalsystem.application.dto.response.ApplicationResponse;
import com.ptit.jobportalsystem.application.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployerApplicationService {

    Page<ApplicationResponse> getApplicantsByJob(Long jobId, Pageable pageable);

//    Page<ApplicationResponse> searchApplicants(Long jobId,
//                                               String keyword,
//                                               ApplicationStatus status,
//                                               Pageable pageable);

    ApplicationResponse updateApplicationStatus(Long applicationId,
                                                UpdateApplicationStatusRequest request);
}