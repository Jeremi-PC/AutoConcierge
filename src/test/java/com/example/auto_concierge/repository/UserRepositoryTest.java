package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.Phone;
import com.example.auto_concierge.entity.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase (connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }
    @Test
    void save() {
        assertTrue(userRepository.findAll().isEmpty());
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Brown");
        user.setEmail("johnbrown@email.com");
        user.setPhoneNumber(List.of(new Phone("+1", "564", "456789")));
       User savedUser = userRepository.save(user);
       User fromDB = userRepository.findById(savedUser.getId()).orElseThrow();
       assertNotNull(fromDB);
       assertNotNull(fromDB.getId());
    }

}