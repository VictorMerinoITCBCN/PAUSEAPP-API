package com.pauseapp.api.dto.activityRecord;

import lombok.Data;

@Data
public class ActivityRecordGetRequest {
    private Long userId;
    private Long activityId;
}
