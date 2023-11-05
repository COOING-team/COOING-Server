package com.example.cooing.domain.answer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAnswerRequest {
    private String answerText;
    private String fileUrl;
}
