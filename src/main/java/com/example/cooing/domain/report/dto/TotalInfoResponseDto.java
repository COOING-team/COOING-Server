package com.example.cooing.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TotalInfoResponseDto {
  private final Long totalWordNum;
  private final String mostUseWord;
}
