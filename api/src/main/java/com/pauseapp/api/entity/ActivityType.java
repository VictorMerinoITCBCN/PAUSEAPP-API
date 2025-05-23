package com.pauseapp.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "activity_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
}
