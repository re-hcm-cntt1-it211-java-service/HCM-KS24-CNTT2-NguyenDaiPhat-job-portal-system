package com.ptit.jobportalsystem.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoleErrorCode implements ErrorCode{
    ROLE_NOT_FOUND(20102, "Không tìm thấy vai trò", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
