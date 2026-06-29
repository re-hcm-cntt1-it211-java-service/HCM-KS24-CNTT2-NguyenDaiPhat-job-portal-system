package com.ptit.jobportalsystem.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode {

    UNAUTHORIZED(2000, "Bạn chưa đăng nhập", HttpStatus.UNAUTHORIZED),

    ACCESS_TOKEN_INVALID(2001, "Access token không hợp lệ", HttpStatus.UNAUTHORIZED),

    ACCESS_TOKEN_EXPIRED(2002, "Access token đã hết hạn", HttpStatus.UNAUTHORIZED),

    TOKEN_REVOKED(2003, "Token đã bị thu hồi", HttpStatus.UNAUTHORIZED),

    REFRESH_TOKEN_REQUIRED(2004, "Refresh token không được để trống", HttpStatus.BAD_REQUEST),

    REFRESH_TOKEN_INVALID(2005, "Refresh token không hợp lệ", HttpStatus.UNAUTHORIZED),

    REFRESH_TOKEN_EXPIRED(2006, "Refresh token đã hết hạn", HttpStatus.UNAUTHORIZED),

    ACCESS_DENIED(2007, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),

    JWT_GENERATION_FAILED(2008, "Không thể tạo JWT", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
