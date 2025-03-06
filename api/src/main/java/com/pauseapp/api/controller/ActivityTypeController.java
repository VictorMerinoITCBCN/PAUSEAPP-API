package com.pauseapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pauseapp.api.dto.activityType.ActivityTypeCreationRequest;
import com.pauseapp.api.entity.ActivityType;
import com.pauseapp.api.repository.ActivityTypeRepository;

@RestController
@RequestMapping("/activity/types")
public class ActivityTypeController {

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @GetMapping
    public ResponseEntity<List<ActivityType>> getActivityTypes() {
        List<ActivityType> activityTypes = activityTypeRepository.findAll();
        return new ResponseEntity<>(activityTypes, HttpStatus.OK);
    } 

    @PostMapping
    public ResponseEntity<ActivityType> createActivityType(@RequestBody ActivityTypeCreationRequest body) {
        ActivityType activityType = new ActivityType();
        activityType.setName(body.getName());
        activityTypeRepository.save(activityType);

        return new ResponseEntity<>(activityType, HttpStatus.CREATED);
    }
}
