package com.example.cooing.global.repository;

import com.example.cooing.global.entity.Answer;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

  int countByBabyId(Long babyId);

  List<Answer> findAllByCreateAtBetween(LocalDateTime start, LocalDateTime end);

  List<Answer> findAllByCreateAtBetweenAndBabyId(LocalDateTime start, LocalDateTime end,
      Long babyId);

  Optional<Answer> findByCreateAtBetweenAndBabyId(LocalDateTime todayMin, LocalDateTime todayMax,
      Long babyId);

  Optional<Answer> findById(Long answerId);
}
