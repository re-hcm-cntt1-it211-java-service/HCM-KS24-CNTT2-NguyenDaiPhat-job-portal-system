package com.ptit.jobportalsystem.security.principal;

import com.ptit.jobportalsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        UserPrincipal principal = userRepository.findByEmailAndIsDeletedFalse(email)
                .map(UserPrincipal::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Không tồn tại email: " + email));

        if (!principal.isEnabled()) {
            throw new DisabledException("Tài khoản đã bị khóa");
        }

        return principal;
    }
}

