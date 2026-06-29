package com.ptit.jobportalsystem.user.service;

import com.ptit.jobportalsystem.user.dto.request.AdminCreateUserRequest;
import com.ptit.jobportalsystem.user.dto.request.AdminUpdateUserRequest;
import com.ptit.jobportalsystem.user.dto.response.UserResponse;
import org.springframework.data.domain.Page;

public interface AdminUserService {

    /**
     * Lấy danh sách người dùng có phân trang
     */
    Page<UserResponse> getUsers(int page, int size);

    /**
     * Lấy thông tin chi tiết người dùng
     */
    UserResponse getUserById(Long id);

    /**
     * Tạo mới người dùng
     */
    UserResponse createUser(AdminCreateUserRequest request);

    /**
     * Cập nhật thông tin người dùng
     */
    UserResponse updateUser(Long id, AdminUpdateUserRequest request);

    /**
     * Xóa mềm người dùng
     */
    void deleteUser(Long id);

}