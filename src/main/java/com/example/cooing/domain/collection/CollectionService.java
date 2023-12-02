package com.example.cooing.domain.collection;

import static com.example.cooing.global.exception.CustomErrorCode.NO_ANSWER;
import static com.example.cooing.global.exception.CustomErrorCode.NO_QUESTION;
import static com.example.cooing.global.util.DateUtil.getMonthEndDate;
import static com.example.cooing.global.util.DateUtil.getMonthStartDate;

import com.example.cooing.domain.collection.dto.AnswerResponseDto;
import com.example.cooing.domain.collection.dto.MonthlyAnswerDto;
import com.example.cooing.global.entity.Answer;
import com.example.cooing.global.entity.Question;
import com.example.cooing.global.exception.CustomException;
import com.example.cooing.global.repository.AnswerRepository;
import com.example.cooing.global.repository.QuestionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionService {

  private final AnswerRepository answerRepository;
  private final QuestionRepository questionRepository;

  public List<MonthlyAnswerDto> getAllCollectionByMonth(Integer year, Integer month) {
    List<Answer> answerList = answerRepository.findAllByCreateAtBetween(
        getMonthStartDate(year, month), getMonthEndDate(year, month));

    // Answer 엔터티를 MonthlyAnswerDto로 변환
    List<MonthlyAnswerDto> monthlyAnswerDtoList = answerList.stream()
        .map(MonthlyAnswerDto::fromAnswer)
        .collect(Collectors.toList());

    return monthlyAnswerDtoList;
  }

  public AnswerResponseDto getAnswer(Long answerId) {
    Answer answer = answerRepository.findById(answerId)
        .orElseThrow(() -> new CustomException(NO_ANSWER));

    Question question = questionRepository.findByCooingIndex(answer.getCooingIndex())
        .orElseThrow(() -> new CustomException(NO_QUESTION));

    return AnswerResponseDto.builder()
        .cooingDay(answerRepository.countByBabyId(answer.getBabyId()))
        .comment(answer.getComment())
        .answerText(answer.getAnswerText())
        .content(question.getContent())
        .fileUrl(answer.getFileUrl())
        .answerId(answerId)
        .createAt(answer.getCreateAt())
        .build();
  }

}
