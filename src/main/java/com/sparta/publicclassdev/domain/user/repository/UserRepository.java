package com.sparta.publicclassdev.domain.user.repository;

import com.sparta.publicclassdev.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
