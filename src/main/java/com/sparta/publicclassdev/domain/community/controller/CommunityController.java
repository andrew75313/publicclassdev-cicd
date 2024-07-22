package com.sparta.publicclassdev.domain.community.controller;

import com.sparta.publicclassdev.domain.community.dto.CommunityRequestDto;
import com.sparta.publicclassdev.domain.community.dto.CommunityResponseDto;
import com.sparta.publicclassdev.domain.community.service.CommunityService;
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
@RequestMapping("/api")
public class CommunityController {
    private final CommunityService service;

    @PostMapping("/community")
    public ResponseEntity<DataResponse<CommunityResponseDto>> createPost(@RequestBody CommunityRequestDto requestDto) {
        CommunityResponseDto responseDto = service.createPost(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse<>(HttpStatus.CREATED.value(), "커뮤니티 게시글 생성", responseDto));
    }
}
