package com.ptit.jobportalsystem.job.service;

import com.ptit.jobportalsystem.job.dto.request.UpdateJobStatusRequest;
import com.ptit.jobportalsystem.job.dto.response.JobResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminJobService {

    Page<JobResponse> getAllJobs(Pageable pageable);

    JobResponse getJobDetail(Long id);

    JobResponse updateJobStatus(Long id, UpdateJobStatusRequest request);

}