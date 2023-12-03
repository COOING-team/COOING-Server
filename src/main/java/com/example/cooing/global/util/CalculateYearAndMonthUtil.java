package com.example.cooing.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CalculateYearAndMonthUtil {
  //년과 월을 입력해서 그 월의 시작 날짜를 반환합니다.
  public static LocalDateTime getMonthStartDate(int year, int month) {
    return LocalDateTime.of(year, month, 1,0,0,0);
  }

  //년과 월을 입력해서 그 월의 마지막 날짜를 반환합니다.
  public static LocalDateTime getMonthEndDate(int year, int month) {
    int lastDayOfMonth = LocalDate.of(year, month, 1).lengthOfMonth();
    return LocalDateTime.of(year, month, lastDayOfMonth,0,0,0);
  }
}
