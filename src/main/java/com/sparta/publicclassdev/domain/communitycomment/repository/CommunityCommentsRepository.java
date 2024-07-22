package com.sparta.publicclassdev.domain.communitycomment.repository;

import com.sparta.publicclassdev.domain.communitycomment.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityCommentsRepository extends JpaRepository<CommunityComment, Long> {

}
