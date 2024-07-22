package com.sparta.publicclassdev.domain.community.repository;

import com.sparta.publicclassdev.domain.community.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}
