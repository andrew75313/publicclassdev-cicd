package com.sparta.publicclassdev.domain.codereview.dto;

import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews;
import com.sparta.publicclassdev.domain.users.entity.Users;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CodeReviewsDetailResponseDto {

  private final Long id;
  private final String name;
  private final String title;
  private final String category;
  private final String code;
  private final LocalDateTime createdAt;
  private final LocalDateTime modifiedAt;

  public CodeReviewsDetailResponseDto(CodeReviews codeReviews, String code, Users user) {
    this.id = codeReviews.getId();
    this.title = codeReviews.getTitle();
    this.name = user.getName();
    this.category = codeReviews.getCategory();
    this.code = code;
    this.createdAt = codeReviews.getCreatedAt();
    this.modifiedAt = codeReviews.getModifiedAt();
  }

}
