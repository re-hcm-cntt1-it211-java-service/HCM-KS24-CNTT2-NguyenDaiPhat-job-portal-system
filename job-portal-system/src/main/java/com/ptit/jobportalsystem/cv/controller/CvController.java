package com.ptit.jobportalsystem.cv.controller;

import com.ptit.jobportalsystem.common.response.ApiResponse;
import com.ptit.jobportalsystem.cv.dto.response.CvResponse;
import com.ptit.jobportalsystem.cv.service.CvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/candidate/cv")
@PreAuthorize("hasRole('CANDIDATE')")
public class CvController {

    private final CvService cvService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<CvResponse>> upload(@RequestParam MultipartFile file) {

        return ResponseEntity.status(201).body(
                ApiResponse.<CvResponse>builder()
                        .message("Upload CV thành công")
                        .data(cvService.uploadCv(file))
                        .build()
        );
    }

    @PutMapping("/{cvId}")
    public ResponseEntity<ApiResponse<CvResponse>> update(
            @PathVariable Long cvId,
            @RequestParam MultipartFile file) {

        return ResponseEntity.ok(
                ApiResponse.<CvResponse>builder()
                        .message("Cập nhật CV thành công")
                        .data(cvService.updateCv(cvId, file))
                        .build()
        );
    }

    @DeleteMapping("/{cvId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long cvId) {

        cvService.deleteCv(cvId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Xóa CV thành công")
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CvResponse>>> getMyCv() {

        return ResponseEntity.ok(
                ApiResponse.<List<CvResponse>>builder()
                        .message("Danh sách CV")
                        .data(cvService.getMyCvs())
                        .build()
        );
    }
}