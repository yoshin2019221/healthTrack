package com.healthtrack.controller;

import com.healthtrack.dto.AuthRequest;
import com.healthtrack.dto.AuthResponse;
import com.healthtrack.entity.User;
import com.healthtrack.repository.UserRepository;
import com.healthtrack.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank())
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        if (request.getPassword() == null || request.getPassword().length() < 6)
            return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters"));
        if (userRepository.existsByEmail(request.getEmail().toLowerCase()))
            return ResponseEntity.badRequest().body(Map.of("error", "Email already registered"));

        User user = new User();
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName() != null ? request.getName() : request.getEmail().split("@")[0]);
        User saved = userRepository.save(user);

        String token = jwtUtil.generateToken(saved.getEmail(), saved.getId());
        return ResponseEntity.ok(new AuthResponse(token, saved.getEmail(), saved.getName(), saved.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        if (request.getEmail() == null || request.getPassword() == null)
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));

        return userRepository.findByEmail(request.getEmail().toLowerCase())
                .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPassword()))
                .map(u -> {
                    String token = jwtUtil.generateToken(u.getEmail(), u.getId());
                    return ResponseEntity.ok(new AuthResponse(token, u.getEmail(), u.getName(), u.getId()));
                })
                .orElse(ResponseEntity.status(401).body(null));
    }
}
