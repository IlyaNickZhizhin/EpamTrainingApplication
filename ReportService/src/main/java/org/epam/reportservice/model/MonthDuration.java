package org.epam.reportservice.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.Month;

@Data
public class MonthDuration implements Comparable<MonthDuration>{
    private Month month;
    private double duration;

    public static MonthDuration of(LocalDate date, double duration){
        MonthDuration monthDuration = new MonthDuration();
        monthDuration.setMonth(date.getMonth());
        monthDuration.setDuration(duration);
        return monthDuration;
    }

    @Override
    public int compareTo(MonthDuration monthDuration) {
        return -this.month.compareTo(monthDuration.month);
    }
}
