package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
