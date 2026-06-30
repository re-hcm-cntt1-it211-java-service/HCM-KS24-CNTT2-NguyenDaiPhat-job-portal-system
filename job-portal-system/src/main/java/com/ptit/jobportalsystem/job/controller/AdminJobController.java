package com.ptit.jobportalsystem.job.controller;

import com.ptit.jobportalsystem.common.response.ApiResponse;
import com.ptit.jobportalsystem.job.dto.request.UpdateJobStatusRequest;
import com.ptit.jobportalsystem.job.dto.response.JobResponse;
import com.ptit.jobportalsystem.job.service.AdminJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminJobController {

    private final AdminJobService adminJobService;

    /**
     * Danh sách tất cả Job
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobResponse>>> getAllJobs(
            Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.<Page<JobResponse>>builder()
                        .message("Lấy danh sách tin tuyển dụng thành công")
                        .data(adminJobService.getAllJobs(pageable))
                        .build()
        );
    }

    /**
     * Chi tiết Job
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> getJobDetail(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<JobResponse>builder()
                        .message("Lấy chi tiết tin tuyển dụng thành công")
                        .data(adminJobService.getJobDetail(id))
                        .build()
        );
    }

    /**
     * Duyệt hoặc từ chối Job
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<JobResponse>> updateJobStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobStatusRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<JobResponse>builder()
                        .message("Cập nhật trạng thái tin tuyển dụng thành công")
                        .data(adminJobService.updateJobStatus(id, request))
                        .build()
        );
    }
}