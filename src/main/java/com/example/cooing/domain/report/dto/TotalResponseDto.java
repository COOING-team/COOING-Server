package com.example.cooing.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TotalResponseDto {
  private final Integer totalWordNum;
  private final String mostUseWord;
}
