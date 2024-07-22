package com.sparta.publicclassdev.domain.community.service;

import com.sparta.publicclassdev.domain.community.dto.CommunityRequestDto;
import com.sparta.publicclassdev.domain.community.dto.CommunityResponseDto;
import com.sparta.publicclassdev.domain.community.entity.Community;
import com.sparta.publicclassdev.domain.community.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository repository;

    public CommunityResponseDto createPost(CommunityRequestDto requestDto) {
        Community community = Community.builder()
            .title(requestDto.getTitle())
            .content(requestDto.getContent())
            .category(requestDto.getCategory())
            .build();

        repository.save(community);

        return new CommunityResponseDto(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory());
    }
}
