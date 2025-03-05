package com.pauseapp.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private ActivityType type;
    
    private String thumbnailURL;

    @OneToOne
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    private Media media;
}
