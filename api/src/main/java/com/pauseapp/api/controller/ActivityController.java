package com.pauseapp.api.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.pauseapp.api.dto.MessageResponse;
import com.pauseapp.api.dto.activity.ActivityCreationRequest;
import com.pauseapp.api.entity.Activity;
import com.pauseapp.api.entity.ActivityRecord;
import com.pauseapp.api.entity.ActivityType;
import com.pauseapp.api.entity.Media;
import com.pauseapp.api.entity.User;
import com.pauseapp.api.repository.ActivityRecordRepository;
import com.pauseapp.api.repository.ActivityRepository;
import com.pauseapp.api.repository.ActivityTypeRepository;
import com.pauseapp.api.repository.MediaRepository;
import com.pauseapp.api.repository.UserRepository;
import com.pauseapp.api.security.JwtUtil;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private ActivityRecordRepository activityRecordRepository;
    
    @Autowired
    private JwtUtil jwtUtil;

    private static final String VIDEO_UPLOAD_DIR = "uploads/videos/";

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

    @GetMapping("/recomended")
    public ResponseEntity<List<Activity>> getRecomendedActivities(@RequestHeader("Authorization") String token) {
        User user = this.getUserByToken(token);
        List<ActivityRecord> lastActivities = activityRecordRepository.findTop3ByUserOrderByTimeDesc(user);

        List<ActivityType> types = new ArrayList<>();
        for (ActivityRecord aRecord: lastActivities) {
            types.add(aRecord.getActivity().getType());
        }

        List<Activity> activities = activityRepository.findByTypeIn(types);

        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @GetMapping("/premium")
    public ResponseEntity<List<Activity>> getPremiumActivities() {
        List<Activity> activities = activityRepository.findByisPremium(true);
        return new ResponseEntity<>(activities, HttpStatus.OK);
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

    public Media uploadMedia(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            Files.createDirectories(Paths.get(VIDEO_UPLOAD_DIR));

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(VIDEO_UPLOAD_DIR, filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Media media = new Media();
            media.setUrl(filePath.toString());
            media.setType("video");
            mediaRepository.save(media);

            return media;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String uploadThumbnail(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            Files.createDirectories(Paths.get(VIDEO_UPLOAD_DIR));

            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(VIDEO_UPLOAD_DIR, filename);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return filePath.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping
    public ResponseEntity<?> createActivity(
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam("typeId") Long typeId,
        @RequestParam("thumbnail") MultipartFile thumbnailFle,
        @RequestParam("media") MultipartFile mediaFile,
        @RequestParam("isPremium") Boolean isPremium
    ) {
        String thumbnailURL = uploadThumbnail(thumbnailFle);
        if (thumbnailURL == null) {
            return ResponseEntity.badRequest().body("Error uploading the thumbnail");
        }
        Media media = uploadMedia(mediaFile);
        if (media == null) {
            return ResponseEntity.badRequest().body("Error uploading the media");
        }
        ActivityType activityType = activityTypeRepository.findById(typeId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ActivityType not found"));


        Activity activity = new Activity();
        activity.setName(name);
        activity.setDescription(description);
        activity.setType(activityType);
        activity.setThumbnailURL(thumbnailURL);
        activity.setMedia(media);
        activity.setIsPremium(isPremium);

        Activity savedActivity = activityRepository.save(activity);

        return new ResponseEntity<>(savedActivity, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteActivity(@PathVariable Long id) {
        activityRepository.deleteById(id);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Activity deleted");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    // @PostMapping
    // public ResponseEntity<Activity> createActivity(@RequestBody ActivityCreationRequest body) {
    //     ActivityType activityType = activityTypeRepository.findById(body.getTypeId())
    //         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ActivityType not found"));
    
    //     Media media = mediaRepository.findById(body.getMediaId())
    //         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found"));
    
    //     Activity activity = new Activity();
    //     activity.setName(body.getName());
    //     activity.setDescription(body.getDescription());
    //     activity.setType(activityType);
    //     activity.setThumbnailURL(body.getThumbnailURL());
    //     activity.setMedia(media);
    //     activity.setIsPremium(body.getIsPremium());
    
    //     activity = activityRepository.save(activity);
    
    //     return ResponseEntity.status(HttpStatus.CREATED).body(activity);
    // }
}
