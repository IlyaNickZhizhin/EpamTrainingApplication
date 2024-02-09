package org.epam.reportservice.model;

import java.time.LocalDate;

public class MonthDuration extends org.epam.common.dto.MonthDuration {
    public static MonthDuration of(LocalDate date, double duration){
        MonthDuration monthDuration = new MonthDuration();
        monthDuration.setMonth(date.getMonth());
        monthDuration.setDuration(duration);
        return monthDuration;
    }
}
