package com.sparta.publicclassdev.domain.user.repository;

import com.sparta.publicclassdev.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

}
