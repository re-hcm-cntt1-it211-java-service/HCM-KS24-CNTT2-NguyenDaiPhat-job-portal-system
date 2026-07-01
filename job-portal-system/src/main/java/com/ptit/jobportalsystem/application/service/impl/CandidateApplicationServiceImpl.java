package com.ptit.jobportalsystem.application.service.impl;

import com.ptit.jobportalsystem.application.dto.request.ApplyJobRequest;
import com.ptit.jobportalsystem.application.dto.response.ApplicationResponse;
import com.ptit.jobportalsystem.application.entity.Application;
import com.ptit.jobportalsystem.application.entity.ApplicationStatus;
import com.ptit.jobportalsystem.application.mapper.ApplicationMapper;
import com.ptit.jobportalsystem.application.repository.ApplicationRepository;
import com.ptit.jobportalsystem.application.service.CandidateApplicationService;
import com.ptit.jobportalsystem.cv.entity.CvFile;
import com.ptit.jobportalsystem.cv.repository.CvFileRepository;
import com.ptit.jobportalsystem.exception.AppException;
import com.ptit.jobportalsystem.exception.ApplicationErrorCode;
import com.ptit.jobportalsystem.exception.JobErrorCode;
import com.ptit.jobportalsystem.exception.UserErrorCode;
import com.ptit.jobportalsystem.job.entity.Job;
import com.ptit.jobportalsystem.job.entity.JobStatus;
import com.ptit.jobportalsystem.job.repository.JobRepository;
import com.ptit.jobportalsystem.security.current.CurrentUserService;
import com.ptit.jobportalsystem.user.entity.User;
import com.ptit.jobportalsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CandidateApplicationServiceImpl implements CandidateApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final CvFileRepository cvFileRepository;
    private final UserRepository userRepository;

    private final ApplicationMapper applicationMapper;
    private final CurrentUserService currentUserService;

    // =========================
    // APPLY JOB
    // =========================
    @Override
    @Transactional
    public ApplicationResponse applyJob(ApplyJobRequest request) {

        Long candidateId = currentUserService.getUserId();

        // 1. check duplicate apply
        if (applicationRepository.existsByCandidateIdAndJobId(candidateId, request.getJobId())) {
            throw new AppException(ApplicationErrorCode.APPLICATION_ALREADY_EXISTS);
        }

        // 2. get candidate
//        User candidate = userRepository.findById(candidateId)
//                .orElseThrow(() -> new AppException(ApplicationErrorCode.APPLICATION_NOT_FOUND));
        User candidate = userRepository.findByIdAndIsActiveTrueAndIsDeletedFalse(candidateId)
                .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        // 3. get job
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new AppException(JobErrorCode.JOB_NOT_FOUND));

        // 4. validate job
        validateJob(job, candidate);

//        // 5. get CV
//        CvFile cv = cvFileRepository.findById(request.getCvId())
//                .orElseThrow(() -> new AppException(ApplicationErrorCode.CV_NOT_BELONG_TO_CANDIDATE));
        CvFile cv = cvFileRepository.findByIdAndIsDeletedFalse(request.getCvId())
                .orElseThrow(() -> new AppException(ApplicationErrorCode.CV_NOT_FOUND));

        // 6. check CV belong to candidate
        if (!cv.getCandidate().getId().equals(candidateId)) {
            throw new AppException(ApplicationErrorCode.CV_NOT_BELONG_TO_CANDIDATE);
        }

        // 7. build application
        Application application = Application.builder()
                .candidate(candidate)
                .job(job)
                .cv(cv)
                .coverLetter(request.getCoverLetter())
                .status(ApplicationStatus.PENDING)
                .build();

        applicationRepository.save(application);

        return applicationMapper.toResponse(application);
    }

    // =========================
    // VIEW MY APPLICATIONS
    // =========================
    @Override
    public Page<ApplicationResponse> getMyApplications(Pageable pageable) {

        Long candidateId = currentUserService.getUserId();

        return applicationRepository.findMyApplications(candidateId, pageable)
                .map(applicationMapper::toResponse);
    }

    // =========================
    // VALIDATE JOB
    // =========================
    private void validateJob(Job job, User candidate) {

        // job deleted
        if (Boolean.TRUE.equals(job.getIsDeleted())) {
            throw new AppException(JobErrorCode.JOB_NOT_FOUND);
        }

        if (job.getStatus() == JobStatus.PENDING) {
            throw new AppException(ApplicationErrorCode.JOB_NOT_APPROVED);
        }

        if (job.getStatus() == JobStatus.REJECTED) {
            throw new AppException(ApplicationErrorCode.JOB_NOT_AVAILABLE);
        }

        // job not approved
        if (job.getStatus() != JobStatus.APPROVED) {
            throw new AppException(ApplicationErrorCode.JOB_NOT_AVAILABLE);
        }

        // job expired
        if (job.getDeadline() != null && job.getDeadline().isBefore(LocalDateTime.now())) {
            throw new AppException(ApplicationErrorCode.JOB_EXPIRED);
        }

        // cannot apply own job
        if (job.getEmployer().getId().equals(candidate.getId())) {
            throw new AppException(ApplicationErrorCode.CANNOT_APPLY_OWN_JOB);
        }
    }
}