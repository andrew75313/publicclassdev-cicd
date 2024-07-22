package com.sparta.publicclassdev.domain.user.service;

import com.sparta.publicclassdev.domain.user.dto.UsersRequestDto;
import com.sparta.publicclassdev.domain.user.dto.UsersResponseDto;
import com.sparta.publicclassdev.domain.user.entity.RoleEnum;
import com.sparta.publicclassdev.domain.user.entity.Users;
import com.sparta.publicclassdev.domain.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    public UsersResponseDto createUser(UsersRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());
        Users user = Users.builder()
            .name(requestDto.getName())
            .email(requestDto.getEmail())
            .password(password)
            .role(RoleEnum.USER)
            .build();
        usersRepository.save(user);
        return UsersResponseDto.builder()
            .name(user.getName())
            .email(user.getEmail())
            .role(user.getRole())
            .build();
    }
}
