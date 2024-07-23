package com.sparta.publicclassdev.domain.users.dto;

import com.sparta.publicclassdev.domain.community.dto.CommunitiesResponseDto;
import com.sparta.publicclassdev.domain.users.entity.Users;
import java.util.List;
import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String name;
    private String email;
    private String intro;
    private List<CommunitiesResponseDto> recentCommunities;

    public ProfileResponseDto(Users user, List<CommunitiesResponseDto> recentCommunities) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.intro = user.getIntro();
        this.recentCommunities = recentCommunities;
    }
}
