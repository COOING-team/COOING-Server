package com.example.cooing.domain.question.dto;


import lombok.Data;

@Data
public class CreateQuestionRequest {

  private String content;
  private Long cooingIndex;
}