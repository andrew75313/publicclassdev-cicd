package com.sparta.publicclassdev.domain.communitycomments.dto;

public class CommunityCommentResponseDto {

    String content;
    Long communityId;

    public CommunityCommentResponseDto(String content, Long communityId) {
        this.content = content;
        this.communityId = communityId;
    }
}
