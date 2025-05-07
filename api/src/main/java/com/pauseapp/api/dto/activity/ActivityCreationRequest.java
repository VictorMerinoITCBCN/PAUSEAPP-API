package com.pauseapp.api.dto.activity;

import lombok.Data;

@Data
public class ActivityCreationRequest {
    private String name;
    private String description;
    private Long typeId;
    private String thumbnailURL;
    private Long mediaId;
    private Boolean isPremium;
}
