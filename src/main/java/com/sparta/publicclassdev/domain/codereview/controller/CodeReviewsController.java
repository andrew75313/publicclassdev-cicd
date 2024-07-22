package com.sparta.publicclassdev.domain.codereview.controller;

import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsRequestDto;
import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsResponseDto;
import com.sparta.publicclassdev.domain.codereview.service.CodeReviewsService;
import com.sparta.publicclassdev.global.dto.DataResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CodeReviewsController {

  private final CodeReviewsService codeReviewsService;

  @PostMapping("/codereviews")
  public ResponseEntity<DataResponse<CodeReviewsResponseDto>> createCodeReview(
      @Valid @RequestBody CodeReviewsRequestDto codeReviewsRequestDto
      , @AuthenticationPrincipal UserDetailsImpl userDetails) {

    CodeReviewsResponseDto response = codeReviewsService.createCodeReview(codeReviewsRequestDto,
        userDetails.getUsers);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new DataResponse<>(201, "코드 리뷰 게시글 등록 완료", response));
  }
}
