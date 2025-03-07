package com.pauseapp.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pauseapp.api.entity.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    
}
