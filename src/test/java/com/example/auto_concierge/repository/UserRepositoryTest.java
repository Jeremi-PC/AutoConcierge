package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.Phone;
import com.example.auto_concierge.entity.user.Role;
import com.example.auto_concierge.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase (connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;



    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("ALTER TABLE users ALTER COLUMN id RESTART WITH 1").executeUpdate();
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
        user.setUsername("johnbrown");
        user.setPassword("password123");
        User savedUser = userRepository.save(user);
       User fromDB = userRepository.findById(savedUser.getId()).orElseThrow();
       assertNotNull(fromDB);
       assertNotNull(fromDB.getId());
    }

    @Test
    void saveUserWithIncompleteData() {
        assertTrue(userRepository.findAll().isEmpty());

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Brown");
        user.setPhoneNumber(List.of(new Phone("+1", "564", "456789")));
        user.setEmail("");

        assertThrows(ConstraintViolationException.class, () -> userRepository.save(user));
    }
    @Test
    void findUserById() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johnbrown");
        user.setPassword("password123");
        userRepository.save(user);

        Optional<User> optionalUser = userRepository.findById(user.getId());
        assertTrue(optionalUser.isPresent());
        assertEquals(user, optionalUser.get());
    }
    @Test
    void findAllUsers() {
        User user1 = new User(1L, "John", "Doe", "johnbrown", "password123", "john.doe@example.com", List.of(new Phone("+1", "564", "456789")), Role.CLIENT, null, null);
        User user2 = new User(2L, "Jane", "Smith", "janesmith", "password456", "jane.smith@example.com", List.of(new Phone("+1", "123", "987654")), Role.SERVICE_CENTER, null, null);

        userRepository.saveAll(List.of(user1, user2));

        List<User> allUsers = userRepository.findAll();

        System.out.println("All users:");
        allUsers.forEach(System.out::println);

        assertFalse(allUsers.isEmpty());
        assertEquals(2, allUsers.size());

        assertEquals(1L, allUsers.get(0).getId());
        assertEquals(2L, allUsers.get(1).getId());
    }


    @Test
    void deleteUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johnbrown");
        user.setPassword("password123");
        userRepository.save(user);

        userRepository.deleteById(user.getId());
        assertFalse(userRepository.findById(user.getId()).isPresent());
    }
    @Test
    void updateUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johnbrown");
        user.setPassword("password123");
        userRepository.save(user);

        Optional<User> optionalUser = userRepository.findById(user.getId());
        assertTrue(optionalUser.isPresent());
        User foundUser = optionalUser.get();

        foundUser.setFirstName("UpdatedFirstName");
        foundUser.setLastName("UpdatedLastName");
        foundUser.setEmail("updated.email@example.com");
        userRepository.save(foundUser);

        Optional<User> updatedUserOptional = userRepository.findById(foundUser.getId());
        assertTrue(updatedUserOptional.isPresent());
        User updatedUser = updatedUserOptional.get();

        assertEquals("UpdatedFirstName", updatedUser.getFirstName());
        assertEquals("UpdatedLastName", updatedUser.getLastName());
        assertEquals("updated.email@example.com", updatedUser.getEmail());
        assertEquals("johnbrown", updatedUser.getUsername());
        assertEquals("password123", updatedUser.getPassword());
    }
}