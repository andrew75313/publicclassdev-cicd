package com.sparta.publicclassdev.domain.communitycomments.repository;

import com.sparta.publicclassdev.domain.communitycomments.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentsRepository extends JpaRepository<CommunityComment, Long> {

}
