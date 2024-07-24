package com.sparta.publicclassdev.domain.teams.service;

import com.sparta.publicclassdev.domain.teams.dto.TeamResponseDto;
import com.sparta.publicclassdev.domain.teams.entity.TeamUsers;
import com.sparta.publicclassdev.domain.teams.entity.Teams;
import com.sparta.publicclassdev.domain.teams.repository.TeamUsersRepository;
import com.sparta.publicclassdev.domain.teams.repository.TeamsRepository;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.users.repository.UsersRepository;
import com.sparta.publicclassdev.global.exception.CustomException;
import com.sparta.publicclassdev.global.exception.ErrorCode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamsService {

    private final TeamsRepository teamsRepository;
    private final TeamUsersRepository teamUsersRepository;
    private final UsersRepository usersRepository;

    private static final List<String> ADJECTIVES = Arrays.asList(
        "Agile", "Brave", "Calm", "Daring", "Eager", "Fierce", "Gentle", "Heroic", "Jolly", "Keen"
    );

    private static final List<String> NOUNS = Arrays.asList(
        "Warriors", "Knights", "Mavericks", "Pioneers", "Rangers", "Samurais", "Titans", "Vikings", "Wizards", "Yankees"
    );

    private static final Random RANDOM = new Random();

    public static String generateRandomTeamName() {
        String adjective = ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(RANDOM.nextInt(NOUNS.size()));
        return adjective + " " + noun;
    }

    @Transactional
    public void teamMatch(Users users) {
        TeamUsers teamUsers = TeamUsers.builder()
            .users(users)
            .build();
        teamUsersRepository.save(teamUsers);
    }

    @Transactional
    public TeamResponseDto createTeam() {
        List<TeamUsers> waitUser = teamUsersRepository.findAll();
        Collections.shuffle(waitUser);

        Integer teamSize = 3;

        Teams teams = Teams.builder().build();
        teamsRepository.save(teams);

        String teamName = "Team " + teams.getId();
        teams.setName(teamName);
        teamsRepository.save(teams);

        List<Users> teamMembers = new ArrayList<>();
        for (int i = 0; i < teamSize && !waitUser.isEmpty(); i++) {
            Users users = waitUser.remove(0).getUsers();
            teamMembers.add(users);

            TeamUsers teamUsers = TeamUsers.builder()
                .teams(teams)
                .users(users)
                .build();
            teamUsersRepository.save(teamUsers);
            teamUsersRepository.delete(teamUsers);
        }
        return new TeamResponseDto(teams, teamMembers);
    }

    public void deleteAllTeams() {
        teamsRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public TeamResponseDto getTeamById(Long teamsId, Long userId) {
        Teams teams = validateTeam(teamsId);
        Users user = validateUser(userId);

        boolean isUserInTeam = teamUsersRepository.existsByTeamsAndUsers(teams, user);
        if (!isUserInTeam) {
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

        List<Users> teamMembers = teams.getTeamUsers().stream()
            .map(teamUsers -> teamUsers.getUsers())
            .collect(Collectors.toList());

        return new TeamResponseDto(teams, teamMembers);
    }


    private Teams validateTeam(Long teamsId) {
        return teamsRepository.findById(teamsId)
            .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
    }

    private Users validateUser(Long userId) {
        return usersRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
