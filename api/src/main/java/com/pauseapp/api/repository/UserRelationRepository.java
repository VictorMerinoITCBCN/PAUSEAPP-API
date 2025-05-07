package com.pauseapp.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pauseapp.api.entity.UserRelation;

public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {
    List<UserRelation> findBySenderId(Long senderId);
    List<UserRelation> findByReceiverId(Long reciverId);
    
    @Query("SELECT ur FROM UserRelation ur WHERE (ur.sender.id = :userId OR ur.receiver.id = :userId) AND ur.status = true")
    List<UserRelation> findAcceptedFriendsByUserId(@Param("userId") Long userId);
}
