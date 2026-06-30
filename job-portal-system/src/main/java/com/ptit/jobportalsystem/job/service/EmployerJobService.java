package com.ptit.jobportalsystem.job.service;

import com.ptit.jobportalsystem.job.dto.request.CreateJobRequest;
import com.ptit.jobportalsystem.job.dto.request.UpdateJobRequest;
import com.ptit.jobportalsystem.job.dto.response.JobResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployerJobService {

    JobResponse createJob(CreateJobRequest request);

    JobResponse updateJob(Long id, UpdateJobRequest request);

    void deleteJob(Long id);

    JobResponse getJobDetail(Long id);

    Page<JobResponse> getMyJobs(Pageable pageable);

}