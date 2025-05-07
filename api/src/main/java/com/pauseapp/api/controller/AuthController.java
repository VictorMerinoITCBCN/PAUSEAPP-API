package com.pauseapp.api.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pauseapp.api.dto.TokenResponse;
import com.pauseapp.api.dto.user.LoginRequest;
import com.pauseapp.api.dto.user.RegisterRequest;
import com.pauseapp.api.entity.ActivityType;
import com.pauseapp.api.entity.User;
import com.pauseapp.api.repository.UserRepository;
import com.pauseapp.api.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).orElse(null) != null) {
            return ResponseEntity.badRequest().body("Email in use");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        user.setSubscription(false);
        user.setStreakDays(0);
        user.setLastActivityDate(LocalDate.now());
        user.setCompletedActivities(0);
        user.setAlertInterval(24);

        userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getEmail());

        TokenResponse response = new TokenResponse();
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user == null) {
            return ResponseEntity.status(404).body("Wrong email");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Wrong password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        TokenResponse response = new TokenResponse();
        response.setToken(token);

        return ResponseEntity.ok(response);
    }
}
