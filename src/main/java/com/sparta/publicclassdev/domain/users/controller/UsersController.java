package com.sparta.publicclassdev.domain.users.controller;

import com.sparta.publicclassdev.domain.users.dto.SignupRequestDto;
import com.sparta.publicclassdev.domain.users.dto.SignupResponseDto;
import com.sparta.publicclassdev.domain.users.service.UsersService;
import com.sparta.publicclassdev.domain.users.dto.LoginRequestDto;
import com.sparta.publicclassdev.domain.users.dto.LoginResponseDto;
import com.sparta.publicclassdev.global.dto.DataResponse;
import com.sparta.publicclassdev.global.dto.MessageResponse;
import com.sparta.publicclassdev.global.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
    private final UsersService usersService;
    @PostMapping("/signup")
    public ResponseEntity<DataResponse<SignupResponseDto>> createUser(@Valid @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = usersService.createUser(requestDto);
        DataResponse<SignupResponseDto> response = new DataResponse<>(HttpStatus.CREATED.value(), "회원가입 성공", responseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        LoginResponseDto responseDto = usersService.login(requestDto);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, responseDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH, responseDto.getRefreshToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}