package com.example.cooing.domain.report.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InfoResponseDto {

  private final int month;
  private final int week;
  private final String name;
  private final int birthMonth;

}