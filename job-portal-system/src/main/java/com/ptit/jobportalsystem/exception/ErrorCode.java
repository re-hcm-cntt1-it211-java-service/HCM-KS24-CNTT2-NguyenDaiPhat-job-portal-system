package com.ptit.jobportalsystem.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    int getCode();
    String getMessage();
    HttpStatus getHttpStatus();
}
