package com.pauseapp.api.dto.user;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name = null;
    private Boolean subscription = null;
    private String initialStressLevel = null;
    private String actualStressLevel = null;
    private Integer streakDays = null;
    private Integer completedActivities = null;
    private Integer alertInterval = null;
}
