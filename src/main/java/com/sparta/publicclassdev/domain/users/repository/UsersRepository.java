package com.sparta.publicclassdev.domain.users.repository;

import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.global.CacheNames;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByName(String name);
    Optional<Users> findByEmail(String email);

}
