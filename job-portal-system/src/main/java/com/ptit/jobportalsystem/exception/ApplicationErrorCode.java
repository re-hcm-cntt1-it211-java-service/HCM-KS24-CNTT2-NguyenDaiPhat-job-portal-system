package com.ptit.jobportalsystem.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {

    // VALIDATION: 4000x
    APPLICATION_JOB_REQUIRED(40001, "Công việc không được để trống", HttpStatus.BAD_REQUEST),
    APPLICATION_CV_REQUIRED(40002, "CV không được để trống", HttpStatus.BAD_REQUEST),
    APPLICATION_COVER_LETTER_INVALID_LENGTH(40003, "Thư giới thiệu không được vượt quá 2000 ký tự", HttpStatus.BAD_REQUEST),
    APPLICATION_STATUS_REQUIRED(40004, "Trạng thái đơn ứng tuyển không được để trống", HttpStatus.BAD_REQUEST),

    // BUSINESS: 4001x
    APPLICATION_NOT_FOUND(40011, "Không tìm thấy đơn ứng tuyển", HttpStatus.NOT_FOUND),
    APPLICATION_ALREADY_EXISTS(40012, "Bạn đã ứng tuyển công việc này", HttpStatus.CONFLICT),
    APPLICATION_STATUS_UNCHANGED(40013, "Trạng thái đơn ứng tuyển không thay đổi", HttpStatus.BAD_REQUEST),
    INVALID_APPLICATION_STATUS(40014, "Trạng thái đơn ứng tuyển không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_APPLICATION_STATUS_TRANSITION(40015, "Không thể chuyển trạng thái đơn ứng tuyển", HttpStatus.BAD_REQUEST),
    APPLICATION_PERMISSION_DENIED(40016, "Bạn không có quyền thao tác với đơn ứng tuyển này", HttpStatus.FORBIDDEN),
    CV_NOT_BELONG_TO_CANDIDATE(40017, "CV không thuộc về ứng viên hiện tại", HttpStatus.BAD_REQUEST),
    CANNOT_APPLY_OWN_JOB(40018, "Không thể ứng tuyển vào công việc của chính mình", HttpStatus.BAD_REQUEST),
    JOB_NOT_AVAILABLE(40019, "Công việc hiện không thể ứng tuyển", HttpStatus.BAD_REQUEST),
    JOB_EXPIRED(40020, "Công việc đã hết hạn ứng tuyển", HttpStatus.BAD_REQUEST),
    APPLICATION_ALREADY_PROCESSED(40021, "Đơn ứng tuyển đã được xử lý", HttpStatus.BAD_REQUEST),
    JOB_NOT_APPROVED(40022, "Công việc chưa được phê duyệt", HttpStatus.BAD_REQUEST),
    CV_NOT_FOUND(40023, "Không tìm thấy CV", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}