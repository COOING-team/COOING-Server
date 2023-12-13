package com.example.cooing.global.exception;


import lombok.Getter;

@Getter
public enum CustomErrorCode {
  // 공통
  OK(0, "성공"),
  FAIL(-1, "실패"),
  CONCURRENCY_ISSUE_DETECTED(-2, "동시성 문제가 발생하였습니다. 재시도해주세요"),

  EXIST_EMAIL(2001, "해당 이메일로 이미 가입한 유저입니다."),

  // mypage
  NO_BABY(3001, "아이를 등록하지 않았습니다."),

  //question
  NO_QUESTION(4001, "해당 인덱스에 대한 질문이 없습니다."),
  EXIST_QUESTION(4002,"이미 존재하는 쿠잉이 질문입니다."),

  //answer
  NO_ANSWER(5001, "해당 답변이 없습니다."),

  //report
  NO_YET_REPORT(6001,"레포트가 생성되지 않아 레포트 요약 정보를 반환할 수 없습니다.");


  private final int code;
  private final String statusMessage;

  CustomErrorCode(int code, String statusMessage) {
    this.code = code;
    this.statusMessage = statusMessage;
  }
}