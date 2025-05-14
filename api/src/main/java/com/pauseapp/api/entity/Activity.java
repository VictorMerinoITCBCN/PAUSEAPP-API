package com.pauseapp.api.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @Column(length = 1000)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private ActivityType type;
    
    private String thumbnailURL;

    @ManyToOne
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    private Media media;

    private Boolean isPremium = false;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ActivityRecord> activityRecords = new ArrayList<>();
}
