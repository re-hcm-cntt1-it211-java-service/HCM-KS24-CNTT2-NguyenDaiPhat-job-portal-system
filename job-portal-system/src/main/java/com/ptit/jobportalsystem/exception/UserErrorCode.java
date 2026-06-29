package com.ptit.jobportalsystem.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    // USER: 2001x
    USER_NOT_FOUND(20011, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),

    EMAIL_ALREADY_EXISTS(20012, "Email đã được sử dụng", HttpStatus.CONFLICT),

    FULL_NAME_REQUIRED(20013, "Họ và tên không được để trống", HttpStatus.BAD_REQUEST),

    FULL_NAME_INVALID_LENGTH(20014, "Họ và tên phải từ 2 đến 100 ký tự", HttpStatus.BAD_REQUEST),

    EMAIL_REQUIRED(20015, "Email không được để trống", HttpStatus.BAD_REQUEST),

    EMAIL_INVALID_FORMAT(20016, "Email không đúng định dạng", HttpStatus.BAD_REQUEST),

    EMAIL_TOO_LONG(20017, "Email không được vượt quá 255 ký tự", HttpStatus.BAD_REQUEST),

    PASSWORD_REQUIRED(20018, "Mật khẩu không được để trống", HttpStatus.BAD_REQUEST),

    PASSWORD_INVALID_LENGTH(20019, "Mật khẩu phải từ 8 đến 100 ký tự", HttpStatus.BAD_REQUEST),

    PASSWORD_WEAK(20020, "Mật khẩu chưa đủ mạnh", HttpStatus.BAD_REQUEST),

    OLD_PASSWORD_INCORRECT(20021, "Mật khẩu hiện tại không chính xác", HttpStatus.BAD_REQUEST),

    OLD_PASSWORD_REQUIRED(20022, "Mật khẩu cũ không được để trống", HttpStatus.BAD_REQUEST),

    NEW_PASSWORD_REQUIRED(20023, "Mật khẩu mới không được để trống", HttpStatus.BAD_REQUEST),

    CONFIRM_PASSWORD_REQUIRED(20024, "Xác nhận mật khẩu không được để trống", HttpStatus.BAD_REQUEST),

    PASSWORD_NOT_MATCH(20025, "Mật khẩu xác nhận không khớp", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
