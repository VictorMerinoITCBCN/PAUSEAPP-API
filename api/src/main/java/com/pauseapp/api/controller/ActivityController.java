package com.pauseapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pauseapp.api.dto.activity.ActivityCreationRequest;
import com.pauseapp.api.entity.Activity;
import com.pauseapp.api.entity.ActivityType;
import com.pauseapp.api.entity.Media;
import com.pauseapp.api.repository.ActivityRepository;
import com.pauseapp.api.repository.ActivityTypeRepository;
import com.pauseapp.api.repository.MediaRepository;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @GetMapping
    public ResponseEntity<List<Activity>> getActivitiesByType(@RequestParam(required = false) List<Long> typeIds) {
        if (typeIds == null || typeIds.isEmpty()) {
            List<Activity> activities = activityRepository.findAll();
            return ResponseEntity.ok(activities);
        }
    
        List<ActivityType> activityTypes = activityTypeRepository.findAllById(typeIds);
    
        if (activityTypes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ActivityType not found");
        }
    
        List<Activity> activities = activityRepository.findByTypeIn(activityTypes);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        Activity activity = activityRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activity not found"));

        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Activity>> getActivitiesByName(@RequestParam String name) {
        List<Activity> activities = activityRepository.findByNameStartingWith(name);

        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityCreationRequest body) {
        ActivityType activityType = activityTypeRepository.findById(body.getTypeId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ActivityType not found"));
    
        Media media = mediaRepository.findById(body.getMediaId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found"));
    
        Activity activity = new Activity();
        activity.setName(body.getName());
        activity.setDescription(body.getDescription());
        activity.setType(activityType);
        activity.setThumbnailURL(body.getThumbnailURL());
        activity.setMedia(media);
    
        activity = activityRepository.save(activity);
    
        return ResponseEntity.status(HttpStatus.CREATED).body(activity);
    }
}
