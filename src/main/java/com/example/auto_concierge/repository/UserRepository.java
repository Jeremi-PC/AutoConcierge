package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    Optional<User> findByUsername(String username);

}
