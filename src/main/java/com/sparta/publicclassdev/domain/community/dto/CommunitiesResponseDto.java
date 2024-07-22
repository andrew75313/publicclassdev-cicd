package com.sparta.publicclassdev.domain.community.dto;

import com.sparta.publicclassdev.domain.community.entity.Communities.Category;
import lombok.Getter;

@Getter
public class CommunitiesResponseDto {
    String title;
    String content;
    Category category;

    public CommunitiesResponseDto(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
