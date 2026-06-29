package com.ptit.jobportalsystem.security.current;

import com.ptit.jobportalsystem.security.principal.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public UserPrincipal getPrincipal() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return (UserPrincipal) authentication.getPrincipal();
    }

    public Long getUserId() {
        return getPrincipal().getId();
    }

    public String getEmail() {
        return getPrincipal().getEmail();
    }

    public String getRole() {
        return getPrincipal().getRole();
    }
}
