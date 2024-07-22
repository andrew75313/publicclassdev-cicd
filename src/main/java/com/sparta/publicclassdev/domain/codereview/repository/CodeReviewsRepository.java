package com.sparta.publicclassdev.domain.codereview.repository;

import com.sparta.publicclassdev.domain.codereview.entity.CodeReviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeReviewsRepository extends JpaRepository<CodeReviews, Long> {

  Page<CodeReviews> findAllWhereStatusIsActiveOrderByCreatedAtDesc(Pageable pageable);
}
