package com.ptit.jobportalsystem.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TokenPair {
    private String accessToken;
    private String refreshToken;
//    private String tokenType;
}
