package com.sparta.publicclassdev.domain.codereview.dto;

import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews;
import com.sparta.publicclassdev.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CodeReviewsResponseDto {
  private final Long id;
  private final String title;
  private final String name;
  private final String category;
  private final String contents;
  private final String code;
  private final LocalDateTime createdAt;

  public CodeReviewsResponseDto(CodeReviews codeReviews, User user) {
    this.id = codeReviews.getId();
    this.title = codeReviews.getTitle();
    this.name = user.getName();
    this.category = codeReviews.getCategory();
    this.contents = codeReviews.getContents();
    this.code = codeReviews.getCode();
    this.createdAt = codeReviews.getCreatedAt();
  }
}
