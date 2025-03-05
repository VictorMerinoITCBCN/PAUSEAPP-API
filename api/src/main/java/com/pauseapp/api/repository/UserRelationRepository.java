package com.pauseapp.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pauseapp.api.entity.UserRelation;

public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {
    List<UserRelation> findBySenderId(Long senderId);
    List<UserRelation> findByReceiverId(Long reciverId);
}
