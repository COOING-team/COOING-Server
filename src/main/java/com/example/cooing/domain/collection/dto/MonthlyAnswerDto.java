package com.example.cooing.domain.collection.dto;

import com.example.cooing.global.entity.Answer;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyAnswerDto {

  private final Long answerId; //얘가 질문 찾는 인덱스
  private final String content; //질문
  private final LocalDateTime createAt;

  // Answer 엔터티를 MonthlyAnswerDto로 변환하는 메서드
  public static MonthlyAnswerDto fromAnswer(Answer answer) {
    return new MonthlyAnswerDto(
        answer.getId(),
        answer.getAnswerText(), // 예시로 답변 텍스트를 content로 사용
        answer.getCreateAt()
        // 다른 필드들도 필요한 경우 추가
    );
  }
}
