package com.sparta.publicclassdev.domain.communitycomments.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class CommunityCommentResponseDto {

    String content;
    Long communityId;

    public CommunityCommentResponseDto(String content, Long communityId) {
        this.content = content;
        this.communityId = communityId;
    }
}
