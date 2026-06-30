package com.ptit.jobportalsystem.job.service.impl;

import com.ptit.jobportalsystem.exception.AppException;
import com.ptit.jobportalsystem.exception.JobErrorCode;
import com.ptit.jobportalsystem.job.dto.request.UpdateJobStatusRequest;
import com.ptit.jobportalsystem.job.dto.response.JobResponse;
import com.ptit.jobportalsystem.job.entity.Job;
import com.ptit.jobportalsystem.job.entity.JobStatus;
import com.ptit.jobportalsystem.job.mapper.JobMapper;
import com.ptit.jobportalsystem.job.repository.JobRepository;
import com.ptit.jobportalsystem.job.service.AdminJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminJobServiceImpl implements AdminJobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Override
    public Page<JobResponse> getAllJobs(Pageable pageable) {

        return jobRepository.findAllByIsDeletedFalse(pageable)
                .map(jobMapper::toResponse);
    }

    @Override
    public JobResponse getJobDetail(Long id) {

        return jobMapper.toResponse(getJob(id));
    }

    @Override
    @Transactional
    public JobResponse updateJobStatus(Long id,
                                       UpdateJobStatusRequest request) {

        Job job = getJob(id);

        validateStatusTransition(job, request.getStatus());

        job.setStatus(request.getStatus());

        return jobMapper.toResponse(jobRepository.save(job));
    }

    /**
     * Lấy Job theo id
     */
    private Job getJob(Long id) {

        return jobRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new AppException(JobErrorCode.JOB_NOT_FOUND));
    }

    /**
     * Kiểm tra việc chuyển trạng thái Job
     */
    private void validateStatusTransition(Job job,
                                          JobStatus newStatus) {

        // Không thay đổi trạng thái
        if (job.getStatus() == newStatus) {
            throw new AppException(JobErrorCode.JOB_STATUS_UNCHANGED);
        }

        // Admin chỉ được APPROVED hoặc REJECTED
        if (newStatus == JobStatus.PENDING
                || newStatus == JobStatus.CLOSED) {
            throw new AppException(JobErrorCode.INVALID_JOB_STATUS);
        }

        // Job đã đóng
        if (job.getStatus() == JobStatus.CLOSED) {
            throw new AppException(JobErrorCode.JOB_ALREADY_CLOSED);
        }

        // Job đã hết hạn
        if (job.getDeadline().isBefore(LocalDateTime.now())) {
            throw new AppException(JobErrorCode.JOB_EXPIRED);
        }

        // Không cho chuyển trực tiếp APPROVED <-> REJECTED
//        if ((job.getStatus() == JobStatus.APPROVED && newStatus == JobStatus.REJECTED)
//                || (job.getStatus() == JobStatus.REJECTED && newStatus == JobStatus.APPROVED)) {
//            throw new AppException(JobErrorCode.INVALID_JOB_STATUS_TRANSITION);
//        }
    }
}