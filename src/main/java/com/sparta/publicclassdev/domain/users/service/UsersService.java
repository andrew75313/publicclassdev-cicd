package com.sparta.publicclassdev.domain.users.service;

import com.sparta.publicclassdev.domain.users.dto.SignupRequestDto;
import com.sparta.publicclassdev.domain.users.dto.SignupResponseDto;
import com.sparta.publicclassdev.domain.users.entity.RoleEnum;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.users.repository.UsersRepository;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_TOKEN}")
    private String ADMIN_TOKEN;
    public SignupResponseDto createUser(SignupRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());
        RoleEnum role = RoleEnum.USER;
        if(requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new CustomException(ErrorCode.INCORRECT_MANAGER_KEY);
            }
            role = RoleEnum.ADMIN;
        }
        Users user = Users.builder()
            .name(requestDto.getName())
            .email(requestDto.getEmail())
            .password(password)
            .role(role)
            .build();
        usersRepository.save(user);
        return SignupResponseDto.builder()
            .name(user.getName())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
    }
}
