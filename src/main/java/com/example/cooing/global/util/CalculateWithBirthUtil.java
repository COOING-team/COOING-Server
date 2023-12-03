package com.example.cooing.global.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

public class CalculateWithBirthUtil {

    //태어난 날을 입력해서 오늘이 생후 몇개월인지를 반환합니다.
    public static int getMonthsSinceBirth(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return (int) period.toTotalMonths();
    }

    //태어날 날을 입력해서 오늘이 몇살인지 반환합니다.
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
