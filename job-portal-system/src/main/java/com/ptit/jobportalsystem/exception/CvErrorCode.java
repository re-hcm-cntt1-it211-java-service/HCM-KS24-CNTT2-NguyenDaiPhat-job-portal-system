package com.ptit.jobportalsystem.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CvErrorCode implements ErrorCode {

    // VALIDATION: 4100x
    CV_FILE_REQUIRED(41001, "File CV không được để trống", HttpStatus.BAD_REQUEST),
    CV_INVALID_FORMAT(41002, "CV phải là file PDF", HttpStatus.BAD_REQUEST),
    CV_FILE_TOO_LARGE(41003, "CV không được vượt quá 15MB", HttpStatus.BAD_REQUEST),

    // BUSINESS: 4101x
    CV_NOT_FOUND(41011, "Không tìm thấy CV", HttpStatus.NOT_FOUND),
    CV_FORBIDDEN(41012, "Bạn không có quyền thao tác CV này", HttpStatus.FORBIDDEN),
    CV_UPLOAD_FAILED(41013, "Upload CV thất bại", HttpStatus.BAD_GATEWAY),
    CV_DELETE_FAILED(41014, "Xoá CV thất bại", HttpStatus.BAD_GATEWAY),

    
    CLOUDINARY_UPLOAD_FAILED(40021, "Upload CV lên Cloud thất bại", HttpStatus.SERVICE_UNAVAILABLE);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}