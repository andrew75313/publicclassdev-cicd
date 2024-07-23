package com.sparta.publicclassdev.domain.codereviewcomment.service;

import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews;
import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews.Status;
import com.sparta.publicclassdev.domain.codereview.repository.CodeReviewsRepository;
import com.sparta.publicclassdev.domain.codereviewcomment.dto.CodeReviewCommentsRequestDto;
import com.sparta.publicclassdev.domain.codereviewcomment.dto.CodeReviewCommentsResponseDto;
import com.sparta.publicclassdev.domain.codereviewcomment.entity.CodeReviewComments;
import com.sparta.publicclassdev.domain.codereviewcomment.repository.CodeReviewCommentsRepository;
import com.sparta.publicclassdev.domain.users.entity.RoleEnum;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.users.repository.UsersRepository;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeReviewCommentsService {

  private final CodeReviewCommentsRepository codeReviewCommentsRepository;
  private final CodeReviewsRepository codeReviewsRepository;
  private final UsersRepository usersRepository;

  public CodeReviewCommentsResponseDto createCodeReviewComment(Long codeReviewsId,
      @Valid CodeReviewCommentsRequestDto codeReviewCommentsResponseDto, Users user) {

    Users foundUser = validateUser(user);

    CodeReviews foundCodeReviews = validateCodeReviewId(codeReviewsId);

    CodeReviewComments codeReviewComment = CodeReviewComments.builder()
        .contents(codeReviewCommentsResponseDto.getContents())
        .status(CodeReviewComments.Status.ACTIVE)
        .user(foundUser)
        .codeReviews(foundCodeReviews)
        .build();

    codeReviewCommentsRepository.save(codeReviewComment);

    return new CodeReviewCommentsResponseDto(codeReviewComment, foundUser);
  }

  public Users validateUser(Users user) {
    Users foundUser = usersRepository.findByEmail(user.getEmail()).orElseThrow(
        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
    );

    if (foundUser.getRole().equals(RoleEnum.WITHDRAW)) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    return foundUser;
  }

  public CodeReviews validateCodeReviewId(Long codeReviewsId) {
    CodeReviews foundCodeReviews = codeReviewsRepository.findById(codeReviewsId).orElseThrow(
        () -> new CustomException(ErrorCode.NOT_FOUND_CODEREVIEW_POST)
    );

    if (foundCodeReviews.getStatus().equals(Status.DELETED)) {
      throw new CustomException(ErrorCode.NOT_FOUND_CODEREVIEW_POST);
    }

    return foundCodeReviews;
  }
}
