package com.pauseapp.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pauseapp.api.entity.ActivityRecord;
import com.pauseapp.api.entity.User;

public interface ActivityRecordRepository extends JpaRepository<ActivityRecord, Long> {
   List<ActivityRecord> findByUserId(Long id);
   Optional<ActivityRecord> findByUserIdAndActivityId(Long userId, Long activityId);
   boolean existsByUserIdAndActivityId(Long userId, Long activityId);
   List<ActivityRecord> findTop3ByUserOrderByTimeDesc(User user);
}
