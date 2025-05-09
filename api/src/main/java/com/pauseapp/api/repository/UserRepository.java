package com.pauseapp.api.repository;

import org.springframework.stereotype.Repository;

import com.pauseapp.api.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByNameStartingWith(String name);
    boolean existsByName(String name);
    List<User> findByNameContainingIgnoreCase(String namePart);
}
