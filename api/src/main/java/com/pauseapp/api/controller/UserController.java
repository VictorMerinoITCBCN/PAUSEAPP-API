package com.pauseapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pauseapp.api.dto.activityRecord.ActivityRecordCreateRequest;
import com.pauseapp.api.dto.activityRecord.ActivityRecordGetRequest;
import com.pauseapp.api.dto.activityRecord.ActivityRecordUpdateRequest;
import com.pauseapp.api.dto.user.UserUpdateRequest;
import com.pauseapp.api.entity.Activity;
import com.pauseapp.api.entity.ActivityRecord;
import com.pauseapp.api.entity.User;
import com.pauseapp.api.repository.ActivityRecordRepository;
import com.pauseapp.api.repository.ActivityRepository;
import com.pauseapp.api.repository.UserRepository;
import com.pauseapp.api.security.JwtUtil;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRecordRepository activityRecordRepository;

    @Autowired
    private ActivityRepository activityRepository;

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
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsersByName(@RequestParam String name) {
        List<User> users = userRepository.findByNameStartingWith(name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> patchUser(@PathVariable Long id, @RequestBody UserUpdateRequest body) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    
        
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
    
    @GetMapping("/{id}/record")
    public ResponseEntity<List<ActivityRecord>> getRecord(@PathVariable Long id) {
        List<ActivityRecord> activityRecord = activityRecordRepository.findByUserId(id);
        
        return new ResponseEntity<>(activityRecord, HttpStatus.OK);
    }

    @GetMapping("/record/")
    public ResponseEntity<ActivityRecord> getRecordByUserIdAndActivityId(@RequestBody ActivityRecordGetRequest body) {
        ActivityRecord activityRecord = activityRecordRepository.findByUserIdAndActivityId(body.getUserId(), body.getActivityId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User or activity not found"));

        return new ResponseEntity<>(activityRecord, HttpStatus.OK);
    }

    @PostMapping("/{id}/record")
    public ResponseEntity<ActivityRecord> createRecord(@PathVariable Long id, @RequestBody ActivityRecordCreateRequest body) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        Activity activity = activityRepository.findById(body.getActivityId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found"));
        
        ActivityRecord activityRecord = new ActivityRecord();
        activityRecord.setUser(user);
        activityRecord.setActivity(activity);
        activityRecord.setStatus(body.getStatus());
        activityRecord.setTime(body.getTime());

        activityRecordRepository.save(activityRecord);
        return new ResponseEntity<>(activityRecord, HttpStatus.CREATED);
    }

    @PatchMapping("/record/{id}")
    public ResponseEntity<ActivityRecord> updateActivityRecord(@PathVariable Long id, @RequestBody ActivityRecordUpdateRequest body) {
        ActivityRecord activityRecord = activityRecordRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ActivityRecord not found"));

        activityRecord.setStatus(body.getStatus());
        activityRecordRepository.save(activityRecord);

        return new ResponseEntity<>(activityRecord, HttpStatus.OK);
    }

}
