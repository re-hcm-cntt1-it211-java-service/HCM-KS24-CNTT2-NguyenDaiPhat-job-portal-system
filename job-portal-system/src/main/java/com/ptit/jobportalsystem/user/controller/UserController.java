package com.ptit.jobportalsystem.user.controller;

import com.ptit.jobportalsystem.common.response.ApiResponse;
import com.ptit.jobportalsystem.user.dto.request.ChangePasswordRequest;
import com.ptit.jobportalsystem.user.dto.request.ForgotPasswordRequest;
import com.ptit.jobportalsystem.user.dto.request.ResetPasswordRequest;
import com.ptit.jobportalsystem.user.dto.request.UpdateProfileRequest;
import com.ptit.jobportalsystem.user.dto.response.UserResponse;
import com.ptit.jobportalsystem.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Xem thông tin người dùng đang đăng nhập
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .message("Lấy thông tin người dùng thành công")
                        .data(userService.getProfile())
                        .build()
        );
    }

    /**
     * Cập nhật thông tin cá nhân
     */
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .message("Cập nhật thông tin thành công")
                        .data(userService.updateProfile(request))
                        .build()
        );
    }

    /**
     * Đổi mật khẩu
     */
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Đổi mật khẩu thành công")
                        .build()
        );
    }

    /**
     * Quên mật khẩu
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        userService.forgotPassword(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Nếu email tồn tại, chúng tôi đã gửi hướng dẫn đặt lại mật khẩu")
                        .build()
        );
    }

    /**
     * Đặt lại mật khẩu
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        userService.resetPassword(request);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Đặt lại mật khẩu thành công")
                        .build()
        );
    }
}
