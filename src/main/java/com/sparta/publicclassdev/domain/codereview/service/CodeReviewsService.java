package com.sparta.publicclassdev.domain.codereview.service;

import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsDetailResponseDto;
import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsListResponseDto;
import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsRequestDto;
import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsResponseDto;
import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsSearchResponseDto;
import com.sparta.publicclassdev.domain.codereview.dto.CodeReviewsWithUserResponseDto;
import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews;
import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews.Status;
import com.sparta.publicclassdev.domain.codereview.repository.CodeReviewsRepository;
import com.sparta.publicclassdev.domain.codereview.util.SizingConstants;
import com.sparta.publicclassdev.domain.codereviewcomment.dto.CodeReviewCommentsWithLikesResponseDto;
import com.sparta.publicclassdev.domain.codereviewcomment.repository.CodeReviewCommentsRepository;
import com.sparta.publicclassdev.domain.users.entity.RoleEnum;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.users.repository.UsersRepository;
import com.sparta.publicclassdev.global.awss3.AwsS3Util;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import io.minio.GetObjectArgs;
import io.minio.PutObjectArgs;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
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
  private final CodeReviewCommentsRepository codeReviewCommentsRepository;
  private final UsersRepository usersRepository;

  private final AwsS3Util awsS3Util;

  @Transactional
  public CodeReviewsResponseDto createCodeReview(CodeReviewsRequestDto codeReviewsRequestDto,
      Users user) {

    Users foundUser = validateUser(user);

    String categories = arrangeCategory(codeReviewsRequestDto.getCategory());

    CodeReviews codeReview = CodeReviews.builder()
        .title(codeReviewsRequestDto.getTitle())
        .category(categories)
        .contents(codeReviewsRequestDto.getContents())
        .status(Status.ACTIVE)
        .user(foundUser)
        .build();

    codeReviewsRepository.save(codeReview);

    String uploadedCode = uploadCodeFile(codeReview.getId(), codeReviewsRequestDto.getCode());

    codeReview.updateCode(uploadedCode);

    return new CodeReviewsResponseDto(codeReview, foundUser);
  }

  public CodeReviewsListResponseDto getAllCodeReviews(int page) {

    Pageable pageable = PageRequest.of(page, SizingConstants.PAGE_SIZE);

    Page<Tuple> codeReviewsPage = codeReviewsRepository.findAllWhereStatusIsActiveOrderByCreatedAtDesc(
        pageable);

    List<CodeReviewsWithUserResponseDto> responseDtoList = codeReviewsPage.getContent().stream()
        .map(CodeReviewsWithUserResponseDto::new)
        .collect(Collectors.toList());

    return new CodeReviewsListResponseDto(
        codeReviewsPage.getPageable().getPageNumber() + 1,
        codeReviewsPage.getTotalPages(),
        codeReviewsPage.getTotalElements(),
        responseDtoList);
  }

  public CodeReviewsDetailResponseDto getCodeReview(Long codeReviewsId) {

    CodeReviews foundCodeReviews = validateCodeReviewId(codeReviewsId);

    String code = downloadCodeFile(foundCodeReviews.getCode());

    List<CodeReviewCommentsWithLikesResponseDto> commentList = codeReviewCommentsRepository.findByCodeReviewIdWithDetails(
            codeReviewsId).stream()
        .map(CodeReviewCommentsWithLikesResponseDto::new)
        .collect(Collectors.toList());

    return new CodeReviewsDetailResponseDto(foundCodeReviews, code, foundCodeReviews.getUser(),
        commentList);
  }

  public CodeReviewsSearchResponseDto getCodeReviewsByCategory(String category, int page) {

    Pageable pageable = PageRequest.of(page, SizingConstants.PAGE_SIZE);

    String searchCategory = arrangeCategory(category);

    Page<Tuple> codeReviewsPage = codeReviewsRepository.findAllByCategory(searchCategory, pageable);

    List<CodeReviewsWithUserResponseDto> responseDtoList = codeReviewsPage.getContent().stream()
        .map(CodeReviewsWithUserResponseDto::new)
        .collect(Collectors.toList());

    return new CodeReviewsSearchResponseDto(
        category,
        codeReviewsPage.getPageable().getPageNumber() + 1,
        codeReviewsPage.getTotalPages(),
        codeReviewsPage.getTotalElements(),
        responseDtoList);
  }

  @Transactional
  public void deleteCodeReview(Long codeReviewsId, Users user) {

    CodeReviews foundCodeReviews = validateCodeReviewId(codeReviewsId);

    Users foundUser = validateUser(user);

    validateOwnership(foundCodeReviews, foundUser);

    foundCodeReviews.delete();
  }

  @Transactional
  public CodeReviewsResponseDto updateCodeReview(CodeReviewsRequestDto codeReviewsRequestDto,
      Long codeReviewsId, Users user) {

    CodeReviews foundCodeReviews = validateCodeReviewId(codeReviewsId);

    Users foundUser = validateUser(user);

    validateOwnership(foundCodeReviews, foundUser);

    foundCodeReviews.updateCodeReview(codeReviewsRequestDto);

    String categories = arrangeCategory(codeReviewsRequestDto.getCategory());

    foundCodeReviews.updateCategory(categories);

    String uploadedCode = uploadCodeFile(foundCodeReviews.getId(), codeReviewsRequestDto.getCode());

    foundCodeReviews.updateCode(uploadedCode);

    return new CodeReviewsResponseDto(foundCodeReviews, foundUser);
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

  public void validateOwnership(CodeReviews codeReviews, Users user) {
    Users writer = codeReviews.getUser();

    if (!writer.getRole().equals(RoleEnum.ADMIN)) {
      if (!writer.getId().equals(user.getId())) {
        throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
      }
    }
  }

  public String arrangeCategory(String category) {

    return "#" + Arrays.stream(category.split("#"))
        .map(s -> s.replace(" ", "").toLowerCase())
        .filter(s -> !s.isEmpty())
        .collect(Collectors.joining(" #")) + " ";
  }

  public String uploadCodeFile(Long codeReviewId, String code) {
    String filename = "codereviews-code/code-" + codeReviewId + ".txt";

    if (code != null && !code.isEmpty()) {
      try {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));

        File tempFile = File.createTempFile("temp", ".txt");
        tempFile.deleteOnExit();
        try (java.io.OutputStream os = new java.io.FileOutputStream(tempFile)) {
          byte[] buffer = new byte[1024];
          int length;
          while ((length = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, length);
          }
        }

        awsS3Util.uploadFile(filename, tempFile);

        return filename;

      } catch (IOException e) {
        throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
      }
    }

    return filename;
  }

  public String downloadCodeFile(String code) {
    String downloadedCode;

    try (InputStream inputStream = awsS3Util.downloadFile(code);
        ByteArrayOutputStream result = new ByteArrayOutputStream()) {

      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) != -1) {
        result.write(buffer, 0, length);
      }

      downloadedCode = result.toString(StandardCharsets.UTF_8.name());
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_DOWNLOAD_FAILED);
    }

    return downloadedCode;
  }
}
