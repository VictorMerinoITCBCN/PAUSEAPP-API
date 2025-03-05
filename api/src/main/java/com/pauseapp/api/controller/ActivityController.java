package com.pauseapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pauseapp.api.dto.ActivityDTO;
import com.pauseapp.api.entity.Activity;
import com.pauseapp.api.entity.ActivityType;
import com.pauseapp.api.entity.Media;
import com.pauseapp.api.repository.ActivityRepository;
import com.pauseapp.api.repository.ActivityTypeRepository;
import com.pauseapp.api.repository.MediaRepository;

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

    @GetMapping("/types")
    public ResponseEntity<List<ActivityType>> getActivityTypes() {
        List<ActivityType> activityTypes = activityTypeRepository.findAll();
        return new ResponseEntity<>(activityTypes, HttpStatus.OK);
    } 

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityDTO body) {
        ActivityType activityType = activityTypeRepository.findById(body.getTypeId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ActiviyType not found"));

        Media media = mediaRepository.findById(body.getMediaId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found"));

        Activity activity = new Activity();
        activity.setName(body.getName());
        activity.setDescription(body.getDescription());
        activity.setType(activityType);
        activity.setThumbnailURL(body.getThumbnailURL());
        activity.setMedia(media);

        activityRepository.save(activity);

        return new ResponseEntity<>(activity, HttpStatus.CREATED);
    }
}
