package com.example.auto_concierge.service;

import com.example.auto_concierge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.auto_concierge.entity.user.User;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Пользователь с адресом электронной почты " + user.getEmail() + " уже существует");
        }
        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании пользователя: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении списка всех пользователей: " + e.getMessage(), e);
        }
    }

    public User getUserById(Long userId) {
        try {
            return userRepository.findById(userId).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении пользователя с идентификатором " + userId + ": " + e.getMessage(), e);
        }
    }

    public User updateUser(Long userId, User userDetails) {
        try {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                user.setFirstName(userDetails.getFirstName());
                user.setLastName(userDetails.getLastName());
                user.setEmail(userDetails.getEmail());
                user.setPassword(userDetails.getPassword());
                user.setRole(userDetails.getRole());
                return userRepository.save(user);
            } else {
                throw new RuntimeException("Пользователь с идентификатором " + userId + " не найден");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении пользователя с идентификатором " + userId + ": " + e.getMessage(), e);
        }
    }

    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(
                user -> userRepository.deleteById(userId),
                () -> {
                    throw new IllegalArgumentException("Пользователь с идентификатором " + userId + " не найден");
                }
        );
    }
}


