package com.ptit.jobportalsystem.cv.cloudinary;

import com.cloudinary.AuthToken;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map<String, Object> uploadFile(MultipartFile file) {
        try {
            String randomPublicId = "cv/" + UUID.randomUUID(); // không đoán được, không trùng tên thật

            Map<String, Object> options = ObjectUtils.asMap(
                    "resource_type", "auto",
                    "type", "upload",
                    "public_id", randomPublicId,
                    "use_filename", false,   // tắt, vì mình tự set public_id rồi
                    "unique_filename", false
            );

            return cloudinary.uploader().upload(file.getBytes(), options);

        } catch (IOException e) {
            throw new RuntimeException("UPLOAD_CV_FAILED: " + e.getMessage(), e);
        }
    }

//    public void deleteFile(String publicId) {
//        try {
//            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "auto"));
//        } catch (IOException e) {
//            throw new RuntimeException("DELETE_CV_FAILED: " + e.getMessage(), e);
//        }
//    }

    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
        } catch (IOException e) {
            throw new RuntimeException("DELETE_CV_FAILED: " + e.getMessage(), e);
        }
    }
}