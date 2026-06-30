package com.ptit.jobportalsystem.job.controller;

import com.ptit.jobportalsystem.common.response.ApiResponse;
import com.ptit.jobportalsystem.job.dto.request.CreateJobRequest;
import com.ptit.jobportalsystem.job.dto.request.UpdateJobRequest;
import com.ptit.jobportalsystem.job.dto.response.JobResponse;
import com.ptit.jobportalsystem.job.service.EmployerJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employer/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerJobController {

    private final EmployerJobService employerJobService;

    /**
     * Đăng tuyển
     */
    @PostMapping
    public ResponseEntity<ApiResponse<JobResponse>> createJob(
            @Valid @RequestBody CreateJobRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<JobResponse>builder()
                        .message("Đăng tin tuyển dụng thành công, vui lòng chờ Admin phê duyệt")
                        .data(employerJobService.createJob(request))
                        .build()
        );
    }

    /**
     * Cập nhật tin tuyển dụng
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<JobResponse>builder()
                        .message("Cập nhật tin tuyển dụng thành công")
                        .data(employerJobService.updateJob(id, request))
                        .build()
        );
    }

    /**
     * Xóa tin tuyển dụng
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @PathVariable Long id) {

        employerJobService.deleteJob(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Xóa tin tuyển dụng thành công")
                        .build()
        );
    }

    /**
     * Xem chi tiết tin tuyển dụng
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobDetail(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<JobResponse>builder()
                        .message("Lấy chi tiết tin tuyển dụng thành công")
                        .data(employerJobService.getJobDetail(id))
                        .build()
        );
    }

    /**
     * Danh sách tin tuyển dụng của tôi
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobResponse>>> getMyJobs(
            Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.<Page<JobResponse>>builder()
                        .message("Lấy danh sách tin tuyển dụng thành công")
                        .data(employerJobService.getMyJobs(pageable))
                        .build()
        );
    }
}