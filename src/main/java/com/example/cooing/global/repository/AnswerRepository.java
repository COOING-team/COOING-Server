package com.example.cooing.global.repository;

import com.example.cooing.global.entity.Answer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

   int countByBabyId(Long babyId);
   List<Answer> findAllByCreateAtBetween(LocalDateTime start, LocalDateTime end);
   Optional<Answer> findByCreateAt(LocalDateTime today);

   Optional<Answer> findById(Long answerId);
}
