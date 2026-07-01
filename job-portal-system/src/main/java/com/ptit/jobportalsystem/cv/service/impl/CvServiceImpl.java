package com.ptit.jobportalsystem.cv.service.impl;

import com.ptit.jobportalsystem.cv.cloudinary.CloudinaryService;
import com.ptit.jobportalsystem.cv.dto.response.CvResponse;
import com.ptit.jobportalsystem.cv.entity.CvFile;
import com.ptit.jobportalsystem.cv.repository.CvFileRepository;
import com.ptit.jobportalsystem.cv.service.CvService;
import com.ptit.jobportalsystem.exception.AppException;
import com.ptit.jobportalsystem.exception.CvErrorCode;
import com.ptit.jobportalsystem.exception.UserErrorCode;
import com.ptit.jobportalsystem.security.current.CurrentUserService;
import com.ptit.jobportalsystem.user.entity.User;
import com.ptit.jobportalsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CvServiceImpl implements CvService {

    private final CvFileRepository cvFileRepository;
    private final CloudinaryService cloudinaryService;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    // ===================== UPLOAD =====================
    @Override
    @Transactional
    public CvResponse uploadCv(MultipartFile file) {
        validateFile(file);

        Long userId = currentUserService.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        Map<String, Object> uploadResult = cloudinaryService.uploadFile(file);

        CvFile cv = CvFile.builder()
                .candidate(user)
                .fileName(file.getOriginalFilename())   // tên gốc, chỉ để hiển thị UI
                .fileUrl(uploadResult.get("secure_url").toString())
                .publicId(uploadResult.get("public_id").toString())
                .isDeleted(false)
                .build();

        CvFile saved = cvFileRepository.save(cv);
        return map(saved);
    }

    // ===================== UPDATE =====================
    @Override
    @Transactional
    public CvResponse updateCv(Long cvId, MultipartFile file) {

        validateFile(file);

        CvFile cv = cvFileRepository.findByIdAndIsDeletedFalse(cvId)
                .orElseThrow(() -> new AppException(CvErrorCode.CV_NOT_FOUND));

        checkOwnership(cv);

        String oldPublicId = cv.getPublicId();

        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file);

            cv.setFileName(file.getOriginalFilename());
            cv.setFileUrl(uploadResult.get("secure_url").toString());
            cv.setPublicId(uploadResult.get("public_id").toString());

            CvFile saved = cvFileRepository.save(cv);

            // delete old file AFTER DB success
            cloudinaryService.deleteFile(oldPublicId);

            return map(saved);

        } catch (Exception e) {
            throw new AppException(CvErrorCode.CLOUDINARY_UPLOAD_FAILED);
        }
    }

    // ===================== DELETE =====================
    @Override
    @Transactional
    public void deleteCv(Long cvId) {

        CvFile cv = cvFileRepository.findByIdAndIsDeletedFalse(cvId)
                .orElseThrow(() -> new AppException(CvErrorCode.CV_NOT_FOUND));

        checkOwnership(cv);

        cv.setIsDeleted(true);
        cvFileRepository.save(cv);

        try {
            cloudinaryService.deleteFile(cv.getPublicId());
        } catch (Exception e) {
            log.warn("Xoá file Cloudinary thất bại, publicId={}, lỗi={}", cv.getPublicId(), e.getMessage());
            // optional: log thôi, không cần fail transaction
        }
    }

    // ===================== GET MY CVS =====================
    @Override
    public List<CvResponse> getMyCvs() {

        Long userId = currentUserService.getUserId();

        return cvFileRepository.findAllByCandidateIdAndIsDeletedFalse(userId)
                .stream()
                .map(this::map)
                .toList();
    }

    // ===================== VALIDATION =====================
    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new AppException(CvErrorCode.CV_FILE_REQUIRED);
        }

        if (file.getOriginalFilename() == null ||
                !file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new AppException(CvErrorCode.CV_INVALID_FORMAT);
        }

        if (file.getSize() > 15 * 1024 * 1024) {
            throw new AppException(CvErrorCode.CV_FILE_TOO_LARGE);
        }

        if (!"application/pdf".equals(file.getContentType())) {
            throw new AppException(CvErrorCode.CV_INVALID_FORMAT);
        }
    }

    // ===================== OWNERSHIP =====================
    private void checkOwnership(CvFile cv) {

        Long currentUserId = currentUserService.getUserId();

        if (!cv.getCandidate().getId().equals(currentUserId)) {
            throw new AppException(CvErrorCode.CV_FORBIDDEN);
        }
    }

    // ===================== MAPPER =====================
    private CvResponse map(CvFile cv) {

        return CvResponse.builder()
                .id(cv.getId())
                .fileName(cv.getFileName())
                .fileUrl(cv.getFileUrl())
                .build();
    }
}