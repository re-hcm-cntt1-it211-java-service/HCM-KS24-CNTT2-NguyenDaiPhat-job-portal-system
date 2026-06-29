package com.ptit.jobportalsystem.auth.service;

import com.ptit.jobportalsystem.auth.dto.request.LoginRequest;
import com.ptit.jobportalsystem.auth.dto.request.RegisterRequest;
import com.ptit.jobportalsystem.auth.dto.response.TokenPair;
import com.ptit.jobportalsystem.exception.AppException;
import com.ptit.jobportalsystem.exception.RoleErrorCode;
import com.ptit.jobportalsystem.exception.UserErrorCode;
import com.ptit.jobportalsystem.security.principal.UserPrincipal;
import com.ptit.jobportalsystem.user.entity.Role;
import com.ptit.jobportalsystem.user.entity.User;
import com.ptit.jobportalsystem.user.repository.RoleRepository;
import com.ptit.jobportalsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.access-token-expiration}")
    private long accessTokenExpirationMs;

    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpirationMs;

    public TokenPair login(LoginRequest request) {
        Authentication auth =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()));

        UserPrincipal user = (UserPrincipal) auth.getPrincipal();

        return tokenService.issueTokenPair(user);
    }

    public void registerCandidate(RegisterRequest request) {
        register(request, "CANDIDATE");
    }

    public void registerEmployer(RegisterRequest request) {
        register(request, "EMPLOYER");
    }

    public void register(RegisterRequest request, String roleName) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(UserErrorCode.EMAIL_ALREADY_EXISTS);

        Role roleUser = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(RoleErrorCode.ROLE_NOT_FOUND));

        userRepository.save(User.builder()
                .role(roleUser)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build());
    }


}
