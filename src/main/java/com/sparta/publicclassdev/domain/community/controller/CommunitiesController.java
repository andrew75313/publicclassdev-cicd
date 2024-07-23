package com.sparta.publicclassdev.domain.community.controller;

import com.sparta.publicclassdev.domain.community.dto.CommunitiesRequestDto;
import com.sparta.publicclassdev.domain.community.dto.CommunitiesResponseDto;
import com.sparta.publicclassdev.domain.community.dto.CommunitiesUpdateRequestDto;
import com.sparta.publicclassdev.domain.community.service.CommunitiesService;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.global.dto.DataResponse;
import com.sparta.publicclassdev.global.dto.MessageResponse;
import com.sparta.publicclassdev.global.security.UserDetailsImpl;
import com.sun.security.auth.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/community/{communityId}")
    public ResponseEntity<DataResponse<CommunitiesResponseDto>> updatePost(@PathVariable Long communityId,@RequestBody CommunitiesUpdateRequestDto requestDto){
        CommunitiesResponseDto responseDto = service.updatePost(communityId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse<>(HttpStatus.OK.value(), "수정 완료", responseDto));
    }

    @DeleteMapping("/community/{communityId}")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable Long communityId){
        service.deletePost(communityId);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(HttpStatus.OK.value(), "삭제 완료"));
    }

    @GetMapping("/community")
    public ResponseEntity<DataResponse<List<CommunitiesResponseDto>>> findPosts(){
        List<CommunitiesResponseDto> responseDto = service.findPosts();
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse<>(HttpStatus.OK.value(), "전체 조회 완료", responseDto));
    }

    @GetMapping("/community/{communityId}")
    public ResponseEntity<DataResponse<CommunitiesResponseDto>> findPost(@PathVariable Long communityId){
        CommunitiesResponseDto responseDto = service.findPost(communityId);
        return ResponseEntity.status(HttpStatus.OK).body(new DataResponse<>(HttpStatus.OK.value(), "단건 조회 완료", responseDto));
    }
}
