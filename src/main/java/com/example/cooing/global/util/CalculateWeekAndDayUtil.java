package com.example.cooing.global.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class CalculateWeekAndDayUtil {

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
}
