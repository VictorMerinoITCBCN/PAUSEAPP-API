package com.pauseapp.api.dto.alert;

import lombok.Data;

@Data
public class AlertCreateRequest {
    private String title;
    private String message;
}
