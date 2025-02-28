package com.pauseapp.api.dto;

import lombok.Data;

@Data
public class UserPatch {
    private String name = null;
    private Boolean subscription = null;
    private String initialStressLevel = null;
    private String actualStressLevel = null;
    private Integer streakDays = null;
    private Integer completedActivities = null;
    private Integer alertInterval = null;
}
