package com.pauseapp.api.dto.userRelation;

import lombok.Data;

@Data
public class UserRelationCreationRequest {
    private Long senderId;
    private Long receiverId;
}
