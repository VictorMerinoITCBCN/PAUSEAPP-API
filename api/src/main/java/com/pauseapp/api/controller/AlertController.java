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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pauseapp.api.dto.alert.AlertCreateRequest;
import com.pauseapp.api.entity.Alert;
import com.pauseapp.api.repository.AlertRepository;

@RestController
@RequestMapping("/alert")
public class AlertController {
    
    @Autowired
    private AlertRepository alertRepository;

    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        List<Alert> alerts = alertRepository.findAll();
        return new ResponseEntity<>(alerts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alert> getAlertById(@PathVariable Long id) {
        Alert alert = alertRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alert not found"));

        return new ResponseEntity<>(alert, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody AlertCreateRequest body) {
        Alert alert = new Alert();
        alert.setTitle(body.getTitle());
        alert.setMessage(body.getMessage());

        alertRepository.save(alert);

        return new ResponseEntity<>(alert, HttpStatus.CREATED);
    }
}
