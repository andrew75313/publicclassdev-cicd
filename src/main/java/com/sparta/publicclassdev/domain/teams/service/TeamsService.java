package com.sparta.publicclassdev.domain.teams.service;

import com.sparta.publicclassdev.domain.chatrooms.entity.ChatRoomUsers;
import com.sparta.publicclassdev.domain.chatrooms.entity.ChatRooms;
import com.sparta.publicclassdev.domain.chatrooms.repository.ChatRoomUsersRepository;
import com.sparta.publicclassdev.domain.chatrooms.repository.ChatRoomsRepository;
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
    private final ChatRoomsRepository chatRoomsRepository;
    private final ChatRoomUsersRepository chatRoomUsersRepository;

    private static final List<String> Modifier = List.of(
        "Agile", "Brave", "Calm", "Daring", "Eager", "Fierce", "Gentle", "Heroic", "Jolly", "Keen"
    );

    private static final List<String> Label = List.of(
        "Warriors", "Knights", "Mavericks", "Pioneers", "Rangers", "Samurais", "Titans", "Vikings",
        "Wizards", "Yankees"
    );

    private final Random RANDOM = new Random();

    private String randomTeamName() {
        String teamName;
        do {
            String modifier = Modifier.get(RANDOM.nextInt(Modifier.size()));
            String label = Label.get(RANDOM.nextInt(Label.size()));
            teamName = modifier + " " + label;
        } while (teamsRepository.existsByName(teamName));
        return teamName;
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

        Teams teams = Teams.builder()
            .name(randomTeamName())
            .build();
        teamsRepository.save(teams);

        ChatRooms chatRooms = ChatRooms.builder()
            .teams(teams)
            .build();
        chatRoomsRepository.save(chatRooms);

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

            ChatRoomUsers chatRoomUsers = ChatRoomUsers.builder()
                .chatRooms(chatRooms)
                .users(users)
                .build();
            chatRoomUsersRepository.save(chatRoomUsers);
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
