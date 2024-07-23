package com.sparta.publicclassdev.domain.community.repository;

import com.sparta.publicclassdev.domain.community.entity.Communities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunitiesRepository extends JpaRepository<Communities, Long> {
}
