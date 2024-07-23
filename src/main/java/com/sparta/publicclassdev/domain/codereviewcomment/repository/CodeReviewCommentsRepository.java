package com.sparta.publicclassdev.domain.codereviewcomment.repository;

import com.sparta.publicclassdev.domain.codereviewcomment.entity.CodeReviewComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeReviewCommentsRepository extends JpaRepository<CodeReviewComments, Long> {

}
