package com.sparta.publicclassdev.domain.community.controller;

import com.sparta.publicclassdev.domain.community.dto.CommunitiesRequestDto;
import com.sparta.publicclassdev.domain.community.dto.CommunitiesResponseDto;
import com.sparta.publicclassdev.domain.community.service.CommunitiesService;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.global.dto.DataResponse;
import com.sparta.publicclassdev.global.security.UserDetailsImpl;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommunitiesController {
    private final CommunitiesService service;

    @PostMapping("/community")
    public ResponseEntity<DataResponse<CommunitiesResponseDto>> createPost(@RequestBody CommunitiesRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Users user = userDetails.getUser();
        CommunitiesResponseDto responseDto = service.createPost(requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse<>(HttpStatus.CREATED.value(), "커뮤니티 게시글 생성", responseDto));
    }
}
