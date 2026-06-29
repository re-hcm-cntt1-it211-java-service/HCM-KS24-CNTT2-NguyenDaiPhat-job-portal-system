package com.ptit.jobportalsystem.user.controller;

import com.ptit.jobportalsystem.common.response.ApiResponse;
import com.ptit.jobportalsystem.user.dto.request.AdminCreateUserRequest;
import com.ptit.jobportalsystem.user.dto.request.AdminUpdateUserRequest;
import com.ptit.jobportalsystem.user.dto.response.UserResponse;
import com.ptit.jobportalsystem.user.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * Lấy danh sách người dùng (có phân trang)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        return ResponseEntity.ok(
                ApiResponse.<Page<UserResponse>>builder()
                        .message("Lấy danh sách người dùng thành công")
                        .data(adminUserService.getUsers(page, size))
                        .build()
        );
    }

    /**
     * Xem chi tiết người dùng
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .message("Lấy thông tin người dùng thành công")
                        .data(adminUserService.getUserById(id))
                        .build()
        );
    }

    /**
     * Tạo mới người dùng
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody AdminCreateUserRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.<UserResponse>builder()
                                .message("Tạo người dùng thành công")
                                .data(adminUserService.createUser(request))
                                .build()
                );
    }

    /**
     * Cập nhật người dùng
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody AdminUpdateUserRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .message("Cập nhật người dùng thành công")
                        .data(adminUserService.updateUser(id, request))
                        .build()
        );
    }

    /**
     * Xóa người dùng (Soft Delete)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id
    ) {

        adminUserService.deleteUser(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .message("Xóa người dùng thành công")
                        .build()
        );
    }
}