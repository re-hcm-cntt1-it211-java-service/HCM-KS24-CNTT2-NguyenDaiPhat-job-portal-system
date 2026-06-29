package com.ptit.jobportalsystem.exception;

import com.ptit.jobportalsystem.common.response.ApiResponse;
import com.ptit.jobportalsystem.user.entity.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(AppException.class)
//    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) {
//
//        ErrorCode errorCode = ex.getErrorCode();
//
//        return ResponseEntity
//                .status(errorCode.getHttpStatus())
//                .body(ApiResponse.<Void>builder()
//                        .code(errorCode.getCode())
//                        .message(errorCode.getMessage())
//                        .build());
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
//
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getFieldErrors()
//                .forEach(err ->
//                        errors.put(err.getField(), err.getDefaultMessage())
//                );
//
//        return ResponseEntity.badRequest()
//                .body(ApiResponse.<Map<String, String>>builder()
//                        .code(400)
//                        .message("Dữ liệu không hợp lệ")
//                        .data(errors)
//                        .build());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleOther(Exception ex) {
//
//        return ResponseEntity.status(500)
//                .body(ApiResponse.<Void>builder()
//                        .code(500)
//                        .message("Lỗi hệ thống")
//                        .build());
//    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) {

        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.<Void>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }


    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.builder()
                        .code(400)
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(err -> {

                    String code = err.getDefaultMessage();
                    ErrorCode errorCode = ErrorCodeResolver.resolve(code);

                    errors.put(
                            err.getField(),
                            errorCode != null
                                    ? errorCode.getMessage()
                                    : "Dữ liệu không hợp lệ"
                    );
                });

        return ResponseEntity.badRequest()
                .body(ApiResponse.<Map<String, String>>builder()
                        .code(400)
                        .message("Validation failed")
                        .data(errors)
                        .build());
    }


    //     fix tạm
//     ngoài lề: để converter sang loại lỗi cụ thể
    static class ErrorCodeResolver {

        static ErrorCode resolve(String code) {

            for (UserErrorCode e : UserErrorCode.values()) {
                if (e.name().equals(code)) return e;
            }

            for (SecurityErrorCode e : SecurityErrorCode.values()) {
                if (e.name().equals(code)) return e;
            }

            for (RoleErrorCode e : RoleErrorCode.values()) {
                if (e.name().equals(code)) return e;
            }

            return null;
        }
    }


}
