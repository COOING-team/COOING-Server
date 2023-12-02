package com.example.cooing.global.repository;

import com.example.cooing.global.entity.Baby;
import com.example.cooing.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BabyRepository extends JpaRepository<Baby, Long> {
    Optional<Baby> findById(Long id);


    Baby findByName(String name);

}
