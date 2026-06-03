package com.neha.infrachallenge.controller;

import com.neha.infrachallenge.entity.User;
import com.neha.infrachallenge.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "service", "infrachallenge-api"
        );
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }
}