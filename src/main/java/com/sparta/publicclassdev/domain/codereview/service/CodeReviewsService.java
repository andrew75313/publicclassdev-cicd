package com.sparta.publicclassdev.domain.codereview.service;

import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsDetailResponseDto;
import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsListResponseDto;
import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsRequestDto;
import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsResponseDto;
import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews;
import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews.Status;
import com.sparta.publicclassdev.domain.codereview.repository.CodeReviewsRepository;
import com.sparta.publicclassdev.domain.codereview.util.SizingConstants;
import com.sparta.publicclassdev.domain.users.entity.RoleEnum;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.users.repository.UsersRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        .collect(Collectors.joining("#"));

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


  public CodeReviewsListResponseDto getAllCodieReviews(int page) {

    Pageable pageable = PageRequest.of(page, SizingConstants.PAGE_SIZE);

    Page<CodeReviews> codeReviewsPage = codeReviewsRepository.findAllWhereStatusIsActiveOrderByCreatedAtDesc(
        pageable);

    List<CodeReviews> codeReviewsList = codeReviewsPage.getContent();

    List<CodeReviewsResponseDto> responseDtoList = codeReviewsList.stream()
        .map(codeReviews -> {
          Users foundUser = usersRepository.findById(codeReviews.getUser().getId())
              .orElseThrow(() -> new NoSuchElementException("사용자가 존재하지 않습니다."));
          return new CodeReviewsResponseDto(codeReviews, foundUser);
        })
        .collect(Collectors.toList());

    return new CodeReviewsListResponseDto(
        codeReviewsPage.getPageable().getPageNumber() + 1,
        codeReviewsPage.getTotalPages(),
        codeReviewsPage.getTotalElements(),
        responseDtoList);
  }

  public CodeReviewsDetailResponseDto getCodeReview(Long codeReviewsId) {

    CodeReviews foundCodeReviews = codeReviewsRepository.findById(codeReviewsId).orElseThrow(
        () -> new NoSuchElementException("코드리뷰가 존재하지 않습니다.")
    );

    if (foundCodeReviews.getStatus().equals(Status.DELETED)) {
      throw new NoSuchElementException("이미 삭제된 코드리뷰입니다.");
    }

    Users foundUser = usersRepository.findById(foundCodeReviews.getUser().getId()).orElseThrow(
        () -> new NoSuchElementException("존재하지 않는 사용자입니다.")
    );

    String code;
    try (InputStream stream = minioClient.getObject(
        GetObjectArgs.builder()
            .bucket("project-dev-bucket")
            .object("codereviews-code/code-" + foundCodeReviews.getId() + ".txt")
            .build())) {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int length;
      while ((length = stream.read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }
      code = result.toString(StandardCharsets.UTF_8.name());
    } catch (Exception e) {
      throw new RuntimeException("파일 다운로드 오류가 발생했습니다.");
    }

    return new CodeReviewsDetailResponseDto(foundCodeReviews, code, foundUser);
  }
}
