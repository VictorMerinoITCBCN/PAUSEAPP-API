package com.pauseapp.api.dto.media;

import lombok.Data;

@Data
public class MediaCreationRequest {
    private String type;
    private String url;
}
