package com.ptit.jobportalsystem.user.service.Impl;

import com.ptit.jobportalsystem.exception.AppException;
import com.ptit.jobportalsystem.exception.UserErrorCode;
import com.ptit.jobportalsystem.security.current.CurrentUserService;
import com.ptit.jobportalsystem.user.dto.request.ChangePasswordRequest;
import com.ptit.jobportalsystem.user.dto.request.ForgotPasswordRequest;
import com.ptit.jobportalsystem.user.dto.request.ResetPasswordRequest;
import com.ptit.jobportalsystem.user.dto.request.UpdateProfileRequest;
import com.ptit.jobportalsystem.user.dto.response.UserResponse;
import com.ptit.jobportalsystem.user.entity.User;
import com.ptit.jobportalsystem.user.mapper.UserMapper;
import com.ptit.jobportalsystem.user.repository.UserRepository;
import com.ptit.jobportalsystem.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    @Override
    public UserResponse getProfile() {

        Long userId = currentUserService.getUserId();
        System.out.println("Phát ktra id " + currentUserService.getUserId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request) {

        Long userId = currentUserService.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        userMapper.update(request, user);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {

        Long userId = currentUserService.getUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        // 1. check old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(UserErrorCode.OLD_PASSWORD_INCORRECT);
        }

        // 2. check confirm password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(UserErrorCode.PASSWORD_NOT_MATCH);
        }

        // 3. encode & save
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new AppException(UserErrorCode.USER_NOT_FOUND));

        // TODO
        // tạo ResetPasswordToken
        // gửi email
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

        // TODO

    }
}

