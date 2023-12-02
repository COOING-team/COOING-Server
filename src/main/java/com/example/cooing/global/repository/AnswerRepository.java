package com.example.cooing.global.repository;

import com.example.cooing.global.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

   int countByBabyId(Long babyId);

}
