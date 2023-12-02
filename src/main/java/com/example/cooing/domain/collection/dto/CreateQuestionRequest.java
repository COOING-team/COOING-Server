package com.example.cooing.domain.collection.dto;


import lombok.Data;

@Data
public class CreateQuestionRequest {

  private String content;
  private Long cooingIndex;
}