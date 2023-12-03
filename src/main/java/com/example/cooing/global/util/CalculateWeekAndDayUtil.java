package com.example.cooing.global.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class CalculateWeekAndDayUtil {

    // n월 m주차에 해당하는 날짜 7개를 리턴합니다. (일 ~ 토)
    public static ArrayList<LocalDate> calculateWeekToDay(Integer month, Integer week) {
        ArrayList<LocalDate> weekOfDays = new ArrayList<>();

        LocalDate firstDayOfMonth = LocalDate.of(LocalDate.now().getYear(), month, 1);

        LocalDate firstMonday = firstDayOfMonth.with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY));

        LocalDate result = firstMonday.plusWeeks(week - 1);

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = result.plusDays(i);
            weekOfDays.add(currentDate);
        }

        return weekOfDays;
    }

    public static WeekOfMonthDto getYearMonthWeekInfo(LocalDate date) {

        int year = date.getYear();
        int month = date.getMonthValue();
        int weekOfMonth = getWeekOfMonth(date);

        return new WeekOfMonthDto(year,month,weekOfMonth);
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
