package com.pauseapp.api.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.pauseapp.api.dto.media.MediaCreationRequest;
import com.pauseapp.api.entity.Media;
import com.pauseapp.api.repository.MediaRepository;
import java.nio.file.Paths;

@RestController
@RequestMapping("/media")
public class MediaController {
    
    @Autowired
    private MediaRepository mediaRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Media> getMedia(@PathVariable Long id) {
        Media media = mediaRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found"));

        return new ResponseEntity<>(media, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Media> createMedia(@RequestBody MediaCreationRequest body) {
        Media media = new Media();
        media.setType(body.getType());
        media.setUrl(body.getUrl());

        mediaRepository.save(media);
        return new ResponseEntity<>(media, HttpStatus.CREATED);
    }
}
