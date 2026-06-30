package com.ptit.jobportalsystem.job.service.impl;

import com.ptit.jobportalsystem.exception.AppException;
import com.ptit.jobportalsystem.exception.JobErrorCode;
import com.ptit.jobportalsystem.job.dto.request.CreateJobRequest;
import com.ptit.jobportalsystem.job.dto.request.UpdateJobRequest;
import com.ptit.jobportalsystem.job.dto.response.JobResponse;
import com.ptit.jobportalsystem.job.entity.Job;
import com.ptit.jobportalsystem.job.entity.JobStatus;
import com.ptit.jobportalsystem.job.mapper.JobMapper;
import com.ptit.jobportalsystem.job.repository.JobRepository;
import com.ptit.jobportalsystem.job.service.EmployerJobService;
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
public class EmployerJobServiceImpl implements EmployerJobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JobMapper jobMapper;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public JobResponse createJob(CreateJobRequest request) {

        User employer = userRepository.getReferenceById(currentUserService.getUserId());

        Job job = jobMapper.toEntity(request);

        job.setEmployer(employer);
        job.setStatus(JobStatus.PENDING);
        job.setIsDeleted(false);

        return jobMapper.toResponse(jobRepository.save(job));
    }

    // lưu ý: Ở updateJob() hiện tại bạn luôn:
    @Override
    @Transactional
    public JobResponse updateJob(Long id, UpdateJobRequest request) {

        Job job = getEmployerJob(id);

        validateJob(job);

//        jobMapper.update(request, job);

        // Employer sửa job => Admin phải duyệt lại
        // bị hash code
        Job oldJob = Job.builder()
                .title(job.getTitle())
                .description(job.getDescription())
                .salary(job.getSalary())
                .location(job.getLocation())
                .deadline(job.getDeadline())
                .build();

        jobMapper.update(request, job);

        if (!oldJob.equals(job)) {
            job.setStatus(JobStatus.PENDING);
        }

        job.setStatus(JobStatus.PENDING);

        return jobMapper.toResponse(jobRepository.save(job));
    }

    @Override
    @Transactional
    public void deleteJob(Long id) {

        Job job = getEmployerJob(id);

//        validateJob(job);

        job.setIsDeleted(true);

        jobRepository.save(job);
    }

    @Override
    public JobResponse getJobDetail(Long id) {

        return jobMapper.toResponse(getEmployerJob(id));
    }

    @Override
    public Page<JobResponse> getMyJobs(Pageable pageable) {

        return jobRepository
                .findAllByEmployerIdAndIsDeletedFalse(
                        currentUserService.getUserId(),
                        pageable)
                .map(jobMapper::toResponse);
    }

    /**
     * Lấy Job của Employer hiện tại
     */
    private Job getEmployerJob(Long jobId) {

        Job job = jobRepository.findByIdAndIsDeletedFalse(jobId)
                .orElseThrow(() ->
                        new AppException(JobErrorCode.JOB_NOT_FOUND));

        if (!job.getEmployer().getId().equals(currentUserService.getUserId())) {
            throw new AppException(JobErrorCode.JOB_PERMISSION_DENIED);
        }

        return job;
    }

    /**
     * Kiểm tra Job còn được phép chỉnh sửa hay không
     */
    private void validateJob(Job job) {

        if (job.getStatus() == JobStatus.CLOSED) {
            throw new AppException(JobErrorCode.JOB_ALREADY_CLOSED);
        }

        if (job.getDeadline().isBefore(LocalDateTime.now())) {
            throw new AppException(JobErrorCode.JOB_EXPIRED);
        }
    }
}