package com.ptit.jobportalsystem.user.service;

import com.ptit.jobportalsystem.user.dto.request.ChangePasswordRequest;
import com.ptit.jobportalsystem.user.dto.request.ForgotPasswordRequest;
import com.ptit.jobportalsystem.user.dto.request.ResetPasswordRequest;
import com.ptit.jobportalsystem.user.dto.request.UpdateProfileRequest;
import com.ptit.jobportalsystem.user.dto.response.UserResponse;

public interface UserService {

    UserResponse getProfile();

    UserResponse updateProfile(UpdateProfileRequest request);

    void changePassword(ChangePasswordRequest request);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

}
