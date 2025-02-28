package com.pauseapp.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pauseapp.api.entity.User;
import com.pauseapp.api.repository.UserRepository;
import com.pauseapp.api.security.JwtUtil;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public User getUserByToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token missing or malformed");
        }

        String jwtToken = token.substring(7);

        String email = jwtUtil.extractEmail(jwtToken);

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token");
        }

        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String token) {
        User user = this.getUserByToken(token);
        System.err.println(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
