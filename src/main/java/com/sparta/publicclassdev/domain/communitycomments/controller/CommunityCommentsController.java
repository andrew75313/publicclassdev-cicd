package com.sparta.publicclassdev.domain.communitycomments.controller;

import com.sparta.publicclassdev.domain.community.dto.CommunitiesRequestDto;
import com.sparta.publicclassdev.domain.community.dto.CommunitiesResponseDto;
import com.sparta.publicclassdev.domain.community.service.CommunitiesService;
import com.sparta.publicclassdev.domain.communitycomments.dto.CommunityCommentResponseDto;
import com.sparta.publicclassdev.domain.communitycomments.dto.CommunityCommentsRequestDto;
import com.sparta.publicclassdev.domain.communitycomments.service.CommunityCommentsService;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.global.dto.DataResponse;
import com.sparta.publicclassdev.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommunityCommentsController {
    private final CommunityCommentsService service;
    @PostMapping("/community/{communityId}/comments")
    public ResponseEntity<DataResponse<CommunityCommentResponseDto>> createComment
        (@PathVariable Long communityId, @RequestBody CommunityCommentsRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Users user = userDetails.getUser();
        CommunityCommentResponseDto responseDto = service.createComment(communityId, requestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse<>(HttpStatus.OK.value(), "댓글 생성 완료", responseDto));
    }
}
