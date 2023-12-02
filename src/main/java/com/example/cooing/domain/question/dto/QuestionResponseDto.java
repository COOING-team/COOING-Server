package com.example.cooing.domain.question.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionResponseDto {

  private final Long questionId; //고유번호
  private final long cooingIndex; //얘가 질문 찾는 인덱스
  private final String content; //질문
}