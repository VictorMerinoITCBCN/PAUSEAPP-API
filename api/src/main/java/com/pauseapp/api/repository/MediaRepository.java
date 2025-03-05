package com.pauseapp.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pauseapp.api.entity.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {
    
}
