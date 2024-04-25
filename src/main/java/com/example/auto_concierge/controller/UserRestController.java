package com.example.auto_concierge.controller;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }
    @RolesAllowed({"ADMIN","CLIENT"})
    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }
    @RolesAllowed({"ADMIN","CLIENT"})
    @GetMapping()
    public List<User> getAllUsers() { return userService.getAllUsers(); }
    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateUser(userId, user);
    }
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

}

