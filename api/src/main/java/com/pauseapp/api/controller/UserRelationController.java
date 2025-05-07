package com.pauseapp.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pauseapp.api.dto.userRelation.UserRelationCreationRequest;
import com.pauseapp.api.dto.userRelation.UserRelationUpdateRequest;
import com.pauseapp.api.entity.User;
import com.pauseapp.api.entity.UserRelation;
import com.pauseapp.api.repository.UserRelationRepository;
import com.pauseapp.api.repository.UserRepository;

@RestController
@RequestMapping("/user/relations")
public class UserRelationController {
    
    @Autowired
    private UserRelationRepository userRelationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/sent/{id}")
    public ResponseEntity<List<UserRelation>> getSentRelations(@PathVariable Long id) {
        List<UserRelation> userRelations = userRelationRepository.findBySenderId(id);
        return new ResponseEntity<>(userRelations, HttpStatus.OK);
    }

    @GetMapping("/received/{id}")
    public ResponseEntity<List<UserRelation>> getReceivedRelations(@PathVariable Long id) {
        List<UserRelation> userRelations = userRelationRepository.findByReceiverId(id);

        List<UserRelation> filteredRelations = userRelations.stream()
            .filter(relation -> !relation.getStatus())
            .collect(Collectors.toList());
        return new ResponseEntity<>(filteredRelations, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserRelation> createRelation(@RequestBody UserRelationCreationRequest body) {
        User sender = userRepository.findById(body.getSenderId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User receiver = userRepository.findById(body.getReceiverId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserRelation userRelation = new UserRelation();
        userRelation.setSender(sender);
        userRelation.setReceiver(receiver);
        userRelation.setStatus(false);

        userRelationRepository.save(userRelation);

        return new ResponseEntity<>(userRelation, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserRelation> updateRelation(@PathVariable Long id,@RequestBody UserRelationUpdateRequest body) {
        UserRelation userRelation = userRelationRepository.findById(id).orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "Relation not found"));

        userRelation.setStatus(body.getStatus());
        userRelationRepository.save(userRelation);

        return new ResponseEntity<>(userRelation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserRelation> deleteUserRelation(@PathVariable Long id) {
        UserRelation userRelation = userRelationRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Relation not found"));

        userRelationRepository.delete(userRelation);
        return new ResponseEntity<>(userRelation, HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long id) {
    
        List<UserRelation> relations = userRelationRepository.findAcceptedFriendsByUserId(id);
    
        List<User> matchingFriends = relations.stream()
            .map(rel -> rel.getSender().getId().equals(id) ? rel.getReceiver() : rel.getSender())
            .toList();

        return new ResponseEntity<>(matchingFriends, HttpStatus.OK);
    }

    @GetMapping("/{id}/friends/search")
    public ResponseEntity<List<User>> searchFriends(@PathVariable Long id, @RequestParam String query) {
    
        List<UserRelation> relations = userRelationRepository.findAcceptedFriendsByUserId(id);
    
        List<User> matchingFriends = relations.stream()
            .map(rel -> rel.getSender().getId().equals(id) ? rel.getReceiver() : rel.getSender())
            .filter(user -> user.getName().toLowerCase().contains(query.toLowerCase()))
            .toList();

        return new ResponseEntity<>(matchingFriends, HttpStatus.OK);
    }
}
