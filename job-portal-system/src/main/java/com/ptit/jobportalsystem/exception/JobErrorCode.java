package com.ptit.jobportalsystem.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JobErrorCode implements ErrorCode {

    JOB_NOT_FOUND(30001, "Không tìm thấy tin tuyển dụng", HttpStatus.NOT_FOUND),

    JOB_TITLE_REQUIRED(30002, "Tiêu đề công việc không được để trống", HttpStatus.BAD_REQUEST),

    JOB_TITLE_INVALID_LENGTH(30003, "Tiêu đề công việc không được vượt quá 255 ký tự", HttpStatus.BAD_REQUEST),

    JOB_DESCRIPTION_REQUIRED(30004, "Mô tả công việc không được để trống", HttpStatus.BAD_REQUEST),

    JOB_LOCATION_REQUIRED(30005, "Địa điểm làm việc không được để trống", HttpStatus.BAD_REQUEST),

    JOB_LOCATION_INVALID_LENGTH(30006, "Địa điểm làm việc không được vượt quá 255 ký tự", HttpStatus.BAD_REQUEST),

    JOB_SALARY_INVALID(30007, "Mức lương phải lớn hơn hoặc bằng 0", HttpStatus.BAD_REQUEST),

    JOB_DEADLINE_REQUIRED(30008, "Hạn nộp hồ sơ không được để trống", HttpStatus.BAD_REQUEST),

    JOB_DEADLINE_INVALID(30009, "Hạn nộp hồ sơ phải sau thời điểm hiện tại", HttpStatus.BAD_REQUEST),

    JOB_EXPIRED(30010, "Tin tuyển dụng đã hết hạn", HttpStatus.BAD_REQUEST),

    JOB_ALREADY_CLOSED(30011, "Tin tuyển dụng đã đóng", HttpStatus.BAD_REQUEST),

    JOB_PERMISSION_DENIED(30012, "Bạn không có quyền thao tác với tin tuyển dụng này", HttpStatus.FORBIDDEN),

    INVALID_JOB_STATUS(30013, "Trạng thái công việc không hợp lệ", HttpStatus.BAD_REQUEST),

    JOB_STATUS_UNCHANGED(30014, "Trạng thái công việc không thay đổi", HttpStatus.BAD_REQUEST),

    INVALID_JOB_STATUS_TRANSITION(30015, "Không thể chuyển sang trạng thái này", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}