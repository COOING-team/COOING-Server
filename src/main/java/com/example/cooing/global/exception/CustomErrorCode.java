package com.example.cooing.global.exception;


import lombok.Getter;

@Getter
public enum CustomErrorCode {
    // 공통
    OK(0, "성공"),
    FAIL(-1, "실패"),
    CONCURRENCY_ISSUE_DETECTED(-2, "동시성 문제가 발생하였습니다. 재시도해주세요"),


    // mypage
    NO_BABY(3001, "아이를 등록하지 않았습니다.");


    private final int code;
    private final String statusMessage;

    CustomErrorCode(int code, String statusMessage) {
        this.code = code;
        this.statusMessage = statusMessage;
    }
}