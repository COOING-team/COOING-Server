package com.example.cooing.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateUtil {
  public static LocalDateTime getMonthStartDate(int year, int month) {
    return LocalDateTime.of(year, month, 1,0,0,0);
  }

  public static LocalDateTime getMonthEndDate(int year, int month) {
    int lastDayOfMonth = LocalDate.of(year, month, 1).lengthOfMonth();
    return LocalDateTime.of(year, month, lastDayOfMonth,0,0,0);
  }
}
