package com.sparta.publicclassdev.domain.community.service;

import com.sparta.publicclassdev.domain.community.dto.CommunitiesRequestDto;
import com.sparta.publicclassdev.domain.community.dto.CommunitiesResponseDto;
import com.sparta.publicclassdev.domain.community.entity.Communities;
import com.sparta.publicclassdev.domain.community.repository.CommunitiesRepository;
import com.sparta.publicclassdev.domain.users.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunitiesService {
    private final CommunitiesRepository repository;

    public CommunitiesResponseDto createPost(CommunitiesRequestDto requestDto, Users user) {
        Communities community = Communities.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .category(requestDto.getCategory())
            .user(user)
            .build();

        repository.save(community);

        return new CommunitiesResponseDto(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory());
    }
}
