package com.ptit.jobportalsystem.application.controller;

import com.ptit.jobportalsystem.application.dto.request.ApplyJobRequest;
import com.ptit.jobportalsystem.application.dto.response.ApplicationResponse;
import com.ptit.jobportalsystem.application.service.CandidateApplicationService;
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
@RequestMapping("/api/v1/candidate/applications")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateApplicationController {

    private final CandidateApplicationService candidateApplicationService;

    /**
     * Apply job
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyJob(
            @Valid @RequestBody ApplyJobRequest request) {

        return ResponseEntity.status(201).body(
                ApiResponse.<ApplicationResponse>builder()
                        .message("Ứng tuyển thành công")
                        .data(candidateApplicationService.applyJob(request))
                        .build()
        );
    }

    /**
     * View my applications
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> getMyApplications(
            Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.<Page<ApplicationResponse>>builder()
                        .message("Lấy danh sách ứng tuyển của bạn thành công")
                        .data(candidateApplicationService.getMyApplications(pageable))
                        .build()
        );
    }
}