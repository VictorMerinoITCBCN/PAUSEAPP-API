package com.pauseapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pauseapp.api.dto.UserPatch;
import com.pauseapp.api.entity.User;
import com.pauseapp.api.entity.UserRelation;
import com.pauseapp.api.repository.UserRelationRepository;
import com.pauseapp.api.repository.UserRepository;
import com.pauseapp.api.security.JwtUtil;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired 
    public UserRelationRepository userRelationRepository;

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

    @GetMapping
    public ResponseEntity<List<User>> getUsersByName(@RequestParam String name) {
        List<User> users = userRepository.findByNameStartingWith(name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable Long id, @RequestBody UserPatch body) {
        User user = userRepository.getReferenceById(id);
        
        if (body.getName() != null) {
            user.setName(body.getName());
        }

        if (body.getSubscription() != null) {
            user.setSubscription(body.getSubscription());
        }

        if (body.getInitialStressLevel() != null) {
            user.setInitialStressLevel(body.getInitialStressLevel());
        }

        if (body.getActualStressLevel() != null) {
            user.setActualStressLevel(body.getActualStressLevel());
        }

        if (body.getStreakDays() != null) {
            user.setStreakDays(body.getStreakDays());
        }

        if (body.getCompletedActivities() != null) {
            user.setCompletedActivities(body.getCompletedActivities());
        }

        if (body.getAlertInterval() != null) {
            user.setAlertInterval(body.getAlertInterval());
        }

        User updatedUser = userRepository.save(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/relations/sent/{id}")
    public ResponseEntity<List<UserRelation>> getSentRelations(@RequestParam Long id) {
        List<UserRelation> userRelations = userRelationRepository.findByUserId(id);
        return new ResponseEntity<>(userRelations, HttpStatus.OK);
    }

    @GetMapping("/relations/recived/{id}")
    public ResponseEntity<List<UserRelation>> getRecivedRelations(@PathVariable Long id) {
        List<UserRelation> userRelations = userRelationRepository.findByFriendId(id);
        return new ResponseEntity<>(userRelations, HttpStatus.OK);
    }

    @GetMapping("/relations/create/{id}")
    public ResponseEntity<> createRelation(@PathVariable Long id) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
