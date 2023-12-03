package com.example.cooing.domain.collection;

import static com.example.cooing.global.exception.CustomErrorCode.NO_ANSWER;
import static com.example.cooing.global.exception.CustomErrorCode.NO_BABY;
import static com.example.cooing.global.exception.CustomErrorCode.NO_QUESTION;
import static com.example.cooing.global.util.CalculateYearAndMonthUtil.getMonthEndDate;
import static com.example.cooing.global.util.CalculateYearAndMonthUtil.getMonthStartDate;

import com.example.cooing.domain.auth.CustomUserDetails;
import com.example.cooing.domain.collection.dto.AnswerResponseDto;
import com.example.cooing.domain.collection.dto.MonthlyAnswerDto;
import com.example.cooing.global.entity.Answer;
import com.example.cooing.global.entity.Baby;
import com.example.cooing.global.entity.Question;
import com.example.cooing.global.entity.User;
import com.example.cooing.global.exception.CustomException;
import com.example.cooing.global.repository.AnswerRepository;
import com.example.cooing.global.repository.BabyRepository;
import com.example.cooing.global.repository.QuestionRepository;
import com.example.cooing.global.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CollectionService {

  private final UserRepository userRepository;
  private final BabyRepository babyRepository;
  private final AnswerRepository answerRepository;
  private final QuestionRepository questionRepository;

  public List<MonthlyAnswerDto> getAllCollectionByMonth(CustomUserDetails userDetail, Integer year, Integer month) {

    User user = userRepository.findByEmail(userDetail.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

    Baby baby = babyRepository.findByUserId(user.getId())
        .orElseThrow(() -> new CustomException(NO_BABY));

    List<Answer> answerList = answerRepository.findAllByCreateAtBetweenAndBabyId(
        getMonthStartDate(year, month), getMonthEndDate(year, month), baby.getId());

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
