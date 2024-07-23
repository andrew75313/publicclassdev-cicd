package com.sparta.publicclassdev.domain.communitycomments.service;

import com.sparta.publicclassdev.domain.community.dto.CommunitiesResponseDto;
import com.sparta.publicclassdev.domain.community.entity.Communities;
import com.sparta.publicclassdev.domain.community.repository.CommunitiesRepository;
import com.sparta.publicclassdev.domain.communitycomments.dto.CommunityCommentResponseDto;
import com.sparta.publicclassdev.domain.communitycomments.dto.CommunityCommentsRequestDto;
import com.sparta.publicclassdev.domain.communitycomments.entity.CommunityComments;
import com.sparta.publicclassdev.domain.communitycomments.repository.CommunityCommentsRepository;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityCommentsService {

    private final CommunityCommentsRepository repository;
    private final CommunitiesRepository communityRepository;
    public CommunityCommentResponseDto createComment(Long communityId, CommunityCommentsRequestDto requestDto, Users user) {
        Communities community = checkCommunity(communityId);

        CommunityComments comment = CommunityComments.builder()
            .content(requestDto.getContents())
            .user(user)
            .community(community)
            .build();

        repository.save(comment);

        return new CommunityCommentResponseDto(requestDto.getContents(), community.getId());
    }

    public CommunityCommentResponseDto updateComment(Long communityId, Long commentId, CommunityCommentsRequestDto requestDto, Users user) {
        Communities community = checkCommunity(communityId);
        CommunityComments comment = checkComment(commentId);

        if(user.equals(comment.getUser())){
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

        comment.updateContent(requestDto.getContents());

        repository.save(comment);

        return new CommunityCommentResponseDto(requestDto.getContents(), community.getId());
    }

    public Communities checkCommunity(Long communityId){
        return communityRepository.findById(communityId).orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_COMMUNITY_POST)
        );
    }

    public CommunityComments checkComment(Long commentId){
        return repository.findById(commentId).orElseThrow(
            () -> new CustomException(ErrorCode.NOT_FOUND_COMMUNITY_COMMENT)
        );
    }
}
