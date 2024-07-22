package com.sparta.publicclassdev.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
