package com.ptit.jobportalsystem.application.controller;

import com.ptit.jobportalsystem.application.dto.request.UpdateApplicationStatusRequest;
import com.ptit.jobportalsystem.application.dto.response.ApplicationResponse;
import com.ptit.jobportalsystem.application.entity.ApplicationStatus;
import com.ptit.jobportalsystem.application.service.EmployerApplicationService;
import com.ptit.jobportalsystem.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employer/applications")
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerApplicationController {

    private final EmployerApplicationService employerApplicationService;

    /**
     * View applicants by job
     */
    @GetMapping("/jobs/{jobId}")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getApplicantsByJob(
            @PathVariable Long jobId,
            Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.<Page<ApplicationResponse>>builder()
                        .message("Lấy danh sách ứng viên thành công")
                        .data(employerApplicationService.getApplicantsByJob(jobId, pageable))
                        .build()
        );
    }

    /**
     * Search applicants
     */
//    @GetMapping("/jobs/{jobId}/search")
//    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> searchApplicants(
//            @PathVariable Long jobId,
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) ApplicationStatus status,
//            Pageable pageable) {
//
//        return ResponseEntity.ok(
//                ApiResponse.<Page<ApplicationResponse>>builder()
//                        .message("Tìm kiếm ứng viên thành công")
//                        .data(employerApplicationService.searchApplicants(jobId, keyword, status, pageable))
//                        .build()
//        );
//    }

    /**
     * Update application status (ACCEPT / REJECT)
     */
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApiResponse<ApplicationResponse>> updateStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody UpdateApplicationStatusRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<ApplicationResponse>builder()
                        .message("Cập nhật trạng thái ứng tuyển thành công")
                        .data(employerApplicationService.updateApplicationStatus(applicationId, request))
                        .build()
        );
    }
}