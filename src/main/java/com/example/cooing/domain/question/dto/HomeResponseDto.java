package com.example.cooing.domain.question.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HomeResponseDto {

  private final String name;

  private final int month;
  private final int cooingDay;
}
