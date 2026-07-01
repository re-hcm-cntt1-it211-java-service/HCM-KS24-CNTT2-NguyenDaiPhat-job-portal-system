package com.ptit.jobportalsystem.cv.service;

import com.ptit.jobportalsystem.cv.dto.response.CvResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CvService {

    CvResponse uploadCv(MultipartFile file);

    CvResponse updateCv(Long cvId, MultipartFile file);

    void deleteCv(Long cvId);

    List<CvResponse> getMyCvs();
}