package com.sparta.publicclassdev.domain.codereview.service;

import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsRequestDto;
import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsResponseDto;
import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews;
import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews.Status;
import com.sparta.publicclassdev.domain.codereview.repository.CodeReviewsRepository;
import com.sparta.publicclassdev.domain.users.entity.RoleEnum;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.users.repository.UsersRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeReviewsService {

  private final CodeReviewsRepository codeReviewsRepository;
  private final UsersRepository usersRepository;

  private final MinioClient minioClient;

  @Transactional
  public CodeReviewsResponseDto createCodeReview(CodeReviewsRequestDto codeReviewsRequestDto,
      Users user) {

    Users foundUser = usersRepository.findByEmail(user.getEmail()).orElseThrow(
        () -> new NoSuchElementException("사용자가 존재하지 않습니다.")
    );

    if (foundUser.getRole().equals(RoleEnum.WITHDRAW)) {
      throw new NoSuchElementException("존재하지 않는 사용자입니다.");
    }

    String categories = "#" + Arrays.stream(codeReviewsRequestDto.getCode().split("#"))
        .map(s -> s.replace(" ", "").toLowerCase())
        .filter(s -> !s.isEmpty())
        .collect(Collectors.joining(" #"));

    CodeReviews codeReview = CodeReviews.builder()
        .title(codeReviewsRequestDto.getTitle())
        .category(categories)
        .contents(codeReviewsRequestDto.getContents())
        .status(Status.ACTIVE)
        .build();

    codeReviewsRepository.save(codeReview);

    String code = codeReviewsRequestDto.getCode();

    if (code != null && !code.isEmpty()) {
      try {
        String filename = "codereviews-code/code-" + codeReview.getId() + ".txt";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(code.getBytes(
            StandardCharsets.UTF_8));

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket("project-dev-bucket")
                .object(filename)
                .stream(inputStream, inputStream.available(), -1)
                .contentType("text/plain")
                .build()
        );

        codeReview.updateCode(filename);
      } catch (Exception e) {
        throw new RuntimeException("파일 업로드 오류가 발생했습니다.");
      }
    }
    return new CodeReviewsResponseDto(codeReview, foundUser);
  }
}
