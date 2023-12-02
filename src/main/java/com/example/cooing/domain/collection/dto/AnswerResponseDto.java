package com.example.cooing.domain.collection.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerResponseDto {

  private final Long answerId; //얘가 질문 찾는 인덱스
  private final String content; //질문

  private final int cooingDay;
  private final LocalDateTime createAt;


  private final String answerText; //쿠잉이의 답변
  private final String fileUrl; //답변 들어보기
  private final String comment; //부모님의 코멘트

}