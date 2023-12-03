package com.example.cooing.global.util;

import java.time.DayOfWeek;
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
    return LocalDateTime.of(year, month, lastDayOfMonth,23,59,59);
  }

  // 해당 월이 몇 주까지 있는지 일요일의 갯수로 계산
  public static Integer getTotalWeekOfMonth(Integer month) {
    LocalDate firstDayOfMonth = LocalDate.of(2023, month, 1);
    LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

    int sundayCount = 0;

    // 일요일이 몇 개 있는지 계산
    while (!firstDayOfMonth.isAfter(lastDayOfMonth)) {
      if (firstDayOfMonth.getDayOfWeek() == DayOfWeek.SUNDAY) {
        sundayCount++;
      }
      firstDayOfMonth = firstDayOfMonth.plusDays(1);
    }

    return sundayCount;
  }
}
