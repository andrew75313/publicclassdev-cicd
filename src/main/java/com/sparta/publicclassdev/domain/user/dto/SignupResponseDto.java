package com.sparta.publicclassdev.domain.user.dto;

import com.sparta.publicclassdev.domain.user.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class SignupResponseDto {
    private String name;
    private String email;
    private RoleEnum role;
}
