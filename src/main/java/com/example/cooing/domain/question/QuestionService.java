package com.example.cooing.domain.question;

import static com.example.cooing.global.exception.CustomErrorCode.EXIST_QUESTION;

import com.example.cooing.domain.question.dto.CreateQuestionRequest;
import com.example.cooing.domain.question.dto.QuestionResponseDto;
import com.example.cooing.global.entity.Question;
import com.example.cooing.global.exception.CustomErrorCode;
import com.example.cooing.global.exception.CustomException;
import com.example.cooing.global.repository.QuestionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository questionRepository;

  public List<Question> getAllQuestion() {

    return questionRepository.findAll();
  }
  public QuestionResponseDto getQuestion(Long cooingIndex) {

    Question question = questionRepository.findByCooingIndex(cooingIndex)
        .orElseThrow(() -> new CustomException(CustomErrorCode.NO_QUESTION));

    return QuestionResponseDto.builder()
        .questionId(question.getId())
        .cooingIndex(question.getCooingIndex())
        .content(question.getContent())
        .build();
  }

  public QuestionResponseDto createQuestion(CreateQuestionRequest createQuestionRequest) {
    // 이미 존재하는지 확인
    Optional<Question> existingQuestion = questionRepository.findByCooingIndex(createQuestionRequest.getCooingIndex());

    if (existingQuestion.isPresent()) {
      // 이미 존재하면 예외 처리
      throw new CustomException(EXIST_QUESTION);
    } else {
      // 없으면 새로 만들어 저장
      Question question = saveQuestion(createQuestionRequest);

      // 새로 만들어진 Question을 반환
      return QuestionResponseDto.builder()
          .questionId(question.getId())
          .cooingIndex(question.getCooingIndex())
          .content(question.getContent())
          .build();
    }
  }

  public Question saveQuestion(CreateQuestionRequest createQuestionRequest) {

    Question q = Question.builder()
        .content(createQuestionRequest.getContent())
        .cooingIndex(createQuestionRequest.getCooingIndex())
        .build();
    questionRepository.saveAndFlush(q);
    return q;
  }

  public void deleteQuestion(Long cooingIndex) {

    //지우려고 하는데 없으면 에러
    Question question = questionRepository.findByCooingIndex(cooingIndex)
        .orElseThrow(() -> new CustomException(CustomErrorCode.NO_QUESTION));

    questionRepository.delete(question);
  }
}
