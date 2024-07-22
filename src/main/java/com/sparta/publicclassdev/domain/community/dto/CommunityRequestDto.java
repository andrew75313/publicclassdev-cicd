package com.sparta.publicclassdev.domain.community.dto;

import com.sparta.publicclassdev.domain.community.entity.Community.Category;
import lombok.Getter;

@Getter
public class CommunityRequestDto {
    String title;
    String content;
    Category category;
}
