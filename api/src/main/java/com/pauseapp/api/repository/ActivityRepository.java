package com.pauseapp.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pauseapp.api.entity.Activity;
import com.pauseapp.api.entity.ActivityType;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByType(ActivityType activityType);
    List<Activity> findByTypeIn(List<ActivityType> activityTypes);
}
