package com.sparta.publicclassdev.domain.user.controller;

import com.sparta.publicclassdev.domain.user.dto.UsersRequestDto;
import com.sparta.publicclassdev.domain.user.dto.UsersResponseDto;
import com.sparta.publicclassdev.domain.user.service.UsersService;
import com.sparta.publicclassdev.global.dto.DataResponse;
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
    public ResponseEntity<DataResponse<UsersResponseDto>> createUser(@RequestBody UsersRequestDto requestDto) {
        UsersResponseDto responseDto = usersService.createUser(requestDto);
        DataResponse<UsersResponseDto> response = new DataResponse<>(HttpStatus.CREATED.value(), "회원가입 성공", responseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
