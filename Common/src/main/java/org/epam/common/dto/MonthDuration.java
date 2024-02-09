package org.epam.common.dto;

import lombok.Data;

import java.time.Month;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Data
public class MonthDuration implements Comparable<MonthDuration>{
    private Month month;
    private double duration;

    @Override
    public int compareTo(MonthDuration monthDuration) {
        return -this.month.compareTo(monthDuration.month);
    }

    public static MonthDuration of(Month date, double duration) {
        MonthDuration monthDuration = new MonthDuration();
        monthDuration.setMonth(date);
        monthDuration.setDuration(duration);
        return monthDuration;
    }

    public static Set<MonthDuration> setOf(Map<Month, Double> monthDoubleMap){
        if (monthDoubleMap==null || monthDoubleMap.isEmpty()) return new TreeSet<>();
        return monthDoubleMap.entrySet().stream()
                .map(e-> MonthDuration.of(e.getKey(), e.getValue()))
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
