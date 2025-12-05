package com.university.coursework.controller;

import com.university.coursework.enums.AdminStatus;
import com.university.coursework.model.DreamUser;
import com.university.coursework.repository.UserRepository;
import com.university.coursework.service.AuthService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody DreamUser user) {
        try {
            authService.register(user);
            logger.info("User {} registered successfully.", user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody DreamUser user) {
        String token = authService.login(user.getUsername(), user.getPassword());
        if (token != null) {
            logger.info("User {} logged in successfully.", user.getUsername());
            DreamUser loggedInUser = authService.getUserByUsername(user.getUsername());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("role", loggedInUser.getRole());

            return ResponseEntity.ok(responseBody);
        } else {
            logger.warn("Failed login attempt for user: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getUserStatus(@RequestParam String username) {
        DreamUser user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        AdminStatus status = authService.getUserStatus(user);
        return ResponseEntity.ok(Map.of("status", status.toString()));
    }
}
