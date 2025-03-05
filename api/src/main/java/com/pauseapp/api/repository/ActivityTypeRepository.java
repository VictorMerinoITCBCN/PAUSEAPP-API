package com.pauseapp.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pauseapp.api.entity.ActivityType;

public interface ActivityTypeRepository extends JpaRepository<ActivityType, Long> {
    
}
