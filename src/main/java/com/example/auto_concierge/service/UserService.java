package com.example.auto_concierge.service;

import com.example.auto_concierge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.auto_concierge.entity.user.User;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        System.out.println("Received POST request to create user: " + user);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateUser(Long userId, User userDetails) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setRole(userDetails.getRole());
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}


