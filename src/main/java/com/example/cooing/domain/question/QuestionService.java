package com.example.cooing.domain.question;

import com.example.cooing.domain.question.dto.QuestionResponseDto;
import com.example.cooing.global.entity.Baby;
import com.example.cooing.global.entity.Question;
import com.example.cooing.global.entity.User;
import com.example.cooing.global.exception.CustomErrorCode;
import com.example.cooing.global.exception.CustomException;
import com.example.cooing.global.repository.AnswerRepository;
import com.example.cooing.global.repository.BabyRepository;
import com.example.cooing.global.repository.QuestionRepository;
import com.example.cooing.global.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository questionRepository;

  public QuestionResponseDto getQuestion(Long cooingIndex) {

    Question question = questionRepository.findByCooingIndex(cooingIndex)
        .orElseThrow(() -> new CustomException(CustomErrorCode.NO_QUESTION));

    return QuestionResponseDto.builder()
        .questionId(question.getId())
        .cooingIndex(question.getCooingIndex())
        .content(question.getContent())
        .build();
  }
}
