package com.sparta.publicclassdev.domain.community.dto;

import com.sparta.publicclassdev.domain.community.entity.Community.Category;
import lombok.Getter;

@Getter
public class CommunityResponseDto {
    String title;
    String content;
    Category category;

    public CommunityResponseDto(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
