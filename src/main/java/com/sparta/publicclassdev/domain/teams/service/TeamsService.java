package com.sparta.publicclassdev.domain.teams.service;

import com.sparta.publicclassdev.domain.teams.dto.TeamCreatesResponseDto;
import com.sparta.publicclassdev.domain.teams.entity.TeamUsers;
import com.sparta.publicclassdev.domain.teams.entity.Teams;
import com.sparta.publicclassdev.domain.teams.repository.TeamUsersRepository;
import com.sparta.publicclassdev.domain.teams.repository.TeamsRepository;
import com.sparta.publicclassdev.domain.users.entity.Users;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamsService {

    private final TeamsRepository teamsRepository;
    private final TeamUsersRepository teamUsersRepository;

    @Transactional
    public void teamMatch(Users users) {
        TeamUsers teamUsers = TeamUsers.builder()
            .users(users)
            .build();
        teamUsersRepository.save(teamUsers);
    }

    @Transactional
    public TeamCreatesResponseDto createTeam() {
        List<TeamUsers> waitUser = teamUsersRepository.findAll();
        Collections.shuffle(waitUser);

        Integer teamSize = 3;

        Teams teams = Teams.builder().build();
        teamsRepository.save(teams);

        String teamName = "Team " + teams.getId();
        teams.setName(teamName);
        teamsRepository.save(teams);

        List<Users> teamMembers = new ArrayList<>();
        for(int i = 0; i < teamSize && !waitUser.isEmpty(); i++) {
            Users users = waitUser.remove(0).getUsers();
            teamMembers.add(users);

            TeamUsers teamUsers = TeamUsers.builder()
                .teams(teams)
                .users(users)
                .build();
            teamUsersRepository.save(teamUsers);
            teamUsersRepository.delete(teamUsers);
        }
        return new TeamCreatesResponseDto(teams, teamMembers);
    }
}
