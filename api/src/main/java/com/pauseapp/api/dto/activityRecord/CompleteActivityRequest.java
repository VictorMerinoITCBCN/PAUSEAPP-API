package com.pauseapp.api.dto.activityRecord;

import lombok.Data;

@Data
public class CompleteActivityRequest {
    private Long userId;
    private Long activityId;
}
