package com.example.cooing.global.util;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WeekOfMonthDto {

  private final int year;
  private final int month;
  private final int weekOfMonth;
}
