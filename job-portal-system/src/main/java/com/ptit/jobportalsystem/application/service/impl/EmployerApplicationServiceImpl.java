package com.ptit.jobportalsystem.application.service.impl;

import com.ptit.jobportalsystem.application.dto.request.UpdateApplicationStatusRequest;
import com.ptit.jobportalsystem.application.dto.response.ApplicationResponse;
import com.ptit.jobportalsystem.application.entity.Application;
import com.ptit.jobportalsystem.application.entity.ApplicationStatus;
import com.ptit.jobportalsystem.application.mapper.ApplicationMapper;
import com.ptit.jobportalsystem.application.repository.ApplicationRepository;
import com.ptit.jobportalsystem.application.service.EmployerApplicationService;
import com.ptit.jobportalsystem.exception.AppException;
import com.ptit.jobportalsystem.exception.ApplicationErrorCode;
import com.ptit.jobportalsystem.exception.JobErrorCode;
import com.ptit.jobportalsystem.job.entity.Job;
import com.ptit.jobportalsystem.job.repository.JobRepository;
import com.ptit.jobportalsystem.security.current.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmployerApplicationServiceImpl implements EmployerApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ApplicationMapper applicationMapper;
    private final CurrentUserService currentUserService;

    // =========================
    // VIEW APPLICANTS (BY JOB)
    // =========================
    @Override
    public Page<ApplicationResponse> getApplicantsByJob(Long jobId, Pageable pageable) {

        Long employerId = currentUserService.getUserId();

//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new AppException(JobErrorCode.JOB_NOT_FOUND));

        Job job = jobRepository.findByIdAndIsDeletedFalse(jobId)
                .orElseThrow(() -> new AppException(JobErrorCode.JOB_NOT_FOUND));

        validateJobOwnership(job, employerId);

        return applicationRepository.findApplicantsByJob(jobId, employerId, pageable)
                .map(applicationMapper::toResponse);
    }

    // =========================
    // SEARCH APPLICANTS
    // =========================
//    @Override
//    public Page<ApplicationResponse> searchApplicants(Long jobId,
//                                                      String keyword,
//                                                      ApplicationStatus status,
//                                                      Pageable pageable) {
//
//        Long employerId = currentUserService.getUserId();
//
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new AppException(ApplicationErrorCode.APPLICATION_NOT_FOUND));
//
//        validateJobOwnership(job, employerId);
//
//        return applicationRepository.searchApplicants(jobId, employerId, keyword, status, pageable)
//                .map(applicationMapper::toResponse);
//    }

    // =========================
    // UPDATE STATUS (ACCEPT / REJECT)
    // =========================
    @Override
    @Transactional
    public ApplicationResponse updateApplicationStatus(Long applicationId,
                                                       UpdateApplicationStatusRequest request) {

        Long employerId = currentUserService.getUserId();

        Application application = applicationRepository.findDetailById(applicationId)
                .orElseThrow(() -> new AppException(ApplicationErrorCode.APPLICATION_NOT_FOUND));

        Job job = application.getJob();

        // check ownership
        validateJobOwnership(job, employerId);

        // check already processed
        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new AppException(ApplicationErrorCode.APPLICATION_ALREADY_PROCESSED);
        }

        // prevent same status
        if (application.getStatus() == request.getStatus()) {
            throw new AppException(ApplicationErrorCode.APPLICATION_STATUS_UNCHANGED);
        }

        // validate status
        if (request.getStatus() != ApplicationStatus.ACCEPTED
                && request.getStatus() != ApplicationStatus.REJECTED) {
            throw new AppException(ApplicationErrorCode.INVALID_APPLICATION_STATUS);
        }

        // update
        application.setStatus(request.getStatus());

        return applicationMapper.toResponse(applicationRepository.save(application));
    }

    // =========================
    // VALIDATE OWNER
    // =========================
    private void validateJobOwnership(Job job, Long employerId) {

        if (!job.getEmployer().getId().equals(employerId)) {
            throw new AppException(ApplicationErrorCode.APPLICATION_PERMISSION_DENIED);
        }
    }
}