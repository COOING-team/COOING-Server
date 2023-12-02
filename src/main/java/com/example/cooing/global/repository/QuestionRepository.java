package com.example.cooing.global.repository;

import com.example.cooing.global.entity.Question;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

   Optional<Question> findById(Long questionId);

   Optional<Question> findByCooingIndex(Long cooingIndex);
}
