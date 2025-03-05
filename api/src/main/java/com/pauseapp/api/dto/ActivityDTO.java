package com.pauseapp.api.dto;

import com.pauseapp.api.entity.ActivityType;
import com.pauseapp.api.entity.Media;

import lombok.Data;

@Data
public class ActivityDTO {
    private String name;
    private String description;
    private Long typeId;
    private String thumbnailURL;
    private Long mediaId;
}
