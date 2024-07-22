package com.sparta.publicclassdev.domain.user.repository;

import com.sparta.publicclassdev.domain.user.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByName(String name);
    Optional<Users> findByEmail(String email);

}
