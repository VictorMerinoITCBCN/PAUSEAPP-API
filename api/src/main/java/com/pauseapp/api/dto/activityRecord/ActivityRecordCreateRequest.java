package com.pauseapp.api.dto.activityRecord;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ActivityRecordCreateRequest {
    private Long activityId;
    private LocalDateTime time;
    private Boolean status;
}
