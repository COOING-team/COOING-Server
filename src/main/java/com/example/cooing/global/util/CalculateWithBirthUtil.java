package com.example.cooing.global.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

public class CalculateWithBirthUtil {

  public static int getMonthsSinceBirth(LocalDate birthDate) {
    LocalDate currentDate = LocalDate.now();
    Period period = Period.between(birthDate, currentDate);
    return (int) period.toTotalMonths();
  }

  public static int getAge(LocalDate birthDate) {
    LocalDate currentDate = LocalDate.now();

    Period period = Period.between(birthDate, currentDate);
    int age = period.getYears();

    // 생일 안 지난 경우 -1
    if (birthDate.plusYears(age).isAfter(currentDate)) {
      age--;
    }

    return age;
  }


  public static String getYearMonthWeekInfo(LocalDate date) {
    int year = date.getYear();
    int month = date.getMonthValue();
    int weekOfMonth = getWeekOfMonth(date);

    return String.format("%d년 %d월 %d주차", year, month, weekOfMonth);
  }

  private static int getWeekOfMonth(LocalDate date) {
    // 주의 시작은 일요일 (DayOfWeek.SUNDAY)
    LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
    int firstDayOfWeekValue = DayOfWeek.SUNDAY.getValue();
    int dayOfWeekValue = firstDayOfMonth.getDayOfWeek().getValue();
    int weekOfMonth = (firstDayOfMonth.getDayOfMonth() + firstDayOfWeekValue - dayOfWeekValue) / 7 + 1;

    return weekOfMonth;
  }

}
