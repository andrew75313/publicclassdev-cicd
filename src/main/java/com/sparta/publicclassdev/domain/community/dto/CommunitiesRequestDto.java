package com.sparta.publicclassdev.domain.community.dto;

import com.sparta.publicclassdev.domain.community.entity.Communities.Category;
import lombok.Getter;

@Getter
public class CommunitiesRequestDto {
    String title;
    String content;
    Category category;
}
