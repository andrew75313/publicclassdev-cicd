package com.sparta.publicclassdev.domain.teams.repository;

import com.sparta.publicclassdev.domain.teams.entity.TeamUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamUsersRepository extends JpaRepository<TeamUsers, Long> {

}
