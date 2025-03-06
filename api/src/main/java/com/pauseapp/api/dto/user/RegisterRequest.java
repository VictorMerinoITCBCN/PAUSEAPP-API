package com.pauseapp.api.dto.user;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;    
}
