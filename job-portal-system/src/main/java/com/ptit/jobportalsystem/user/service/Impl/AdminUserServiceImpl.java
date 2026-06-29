package com.ptit.jobportalsystem.user.service.Impl;

import com.ptit.jobportalsystem.exception.AppException;
import com.ptit.jobportalsystem.exception.RoleErrorCode;
import com.ptit.jobportalsystem.exception.UserErrorCode;
import com.ptit.jobportalsystem.user.dto.request.AdminCreateUserRequest;
import com.ptit.jobportalsystem.user.dto.request.AdminUpdateUserRequest;
import com.ptit.jobportalsystem.user.dto.response.UserResponse;
import com.ptit.jobportalsystem.user.entity.Role;
import com.ptit.jobportalsystem.user.entity.User;
import com.ptit.jobportalsystem.user.mapper.UserMapper;
import com.ptit.jobportalsystem.user.repository.RoleRepository;
import com.ptit.jobportalsystem.user.repository.UserRepository;
import com.ptit.jobportalsystem.user.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> getUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findAllByIsDeletedFalse(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new AppException(UserErrorCode.USER_NOT_FOUND));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse createUser(AdminCreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() ->
                        new AppException(RoleErrorCode.ROLE_NOT_FOUND));

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, AdminUpdateUserRequest request) {

        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new AppException(UserErrorCode.USER_NOT_FOUND));

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() ->
                        new AppException(RoleErrorCode.ROLE_NOT_FOUND));

        user.setFullName(request.getFullName());
        user.setRole(role);
        user.setIsActive(request.getIsActive());

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() ->
                        new AppException(UserErrorCode.USER_NOT_FOUND));

        user.setIsDeleted(true);

        userRepository.save(user);
    }
}