package com.sparta.publicclassdev.domain.teams.dto;

import com.sparta.publicclassdev.domain.teams.entity.Teams;
import com.sparta.publicclassdev.domain.users.entity.Users;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class TeamCreatesResponseDto {

    private Long id;
    private String teamsName;
    private List<String> teamMembers;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public TeamCreatesResponseDto(Teams teams, List<Users> users) {

        this.id = teams.getId();
        this.teamsName = teams.getName();
        List<String> memberName = new ArrayList<>();
        for (Users user : users) {
            memberName.add(user.getName());
        }
        this.teamMembers = memberName;
        this.createdAt = teams.getCreatedAt();
        this.modifiedAt = teams.getModifiedAt();
    }
}
