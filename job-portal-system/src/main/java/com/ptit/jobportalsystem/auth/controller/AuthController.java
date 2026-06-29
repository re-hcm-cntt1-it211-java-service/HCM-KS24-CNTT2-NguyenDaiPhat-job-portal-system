package com.ptit.jobportalsystem.auth.controller;

import com.ptit.jobportalsystem.auth.dto.request.LoginRequest;
import com.ptit.jobportalsystem.auth.dto.request.LogoutRequest;
import com.ptit.jobportalsystem.auth.dto.request.RefreshRequest;
import com.ptit.jobportalsystem.auth.dto.request.RegisterRequest;
import com.ptit.jobportalsystem.auth.dto.response.TokenPair;
import com.ptit.jobportalsystem.auth.service.AuthService;
import com.ptit.jobportalsystem.auth.service.TokenService;
import com.ptit.jobportalsystem.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse<TokenPair>> login(@Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok()
                .body(ApiResponse.<TokenPair>builder()
                        .message("Đăng nhập thành công")
                        .data(authService.login(request))
                        .build());
    }

    @PostMapping("/register/candidate")
    public ResponseEntity<ApiResponse<Void>> registerCandidate(
            @Valid @RequestBody RegisterRequest request) {

        authService.registerCandidate(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>builder()
                        .message("Đăng ký tài khoản ứng viên thành công")
                        .build());
    }

    @PostMapping("/register/employer")
    public ResponseEntity<ApiResponse<Void>> registerEmployer(
            @Valid @RequestBody RegisterRequest request) {

        authService.registerEmployer(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>builder()
                        .message("Đăng ký tài khoản nhà tuyển dụng thành công")
                        .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenPair>> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok()
                .body(ApiResponse.<TokenPair>builder()
                        .message("Làm mới token thành công")
                        .data(tokenService.refresh(request.getRefreshToken()))
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody LogoutRequest request
    ) {

        String accessToken = bearerToken.substring(7);

        tokenService.logout(accessToken, request.getRefreshToken());

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Đăng xuất thành công")
                        .build()
        );
    }
}
