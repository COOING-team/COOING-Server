package com.example.cooing.global.util;

import java.time.LocalDate;
import java.time.Period;

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

}
