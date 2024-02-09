package org.epam.common.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class TrainingSession implements Comparable<TrainingSession>{
    private int year;
    private Set<MonthDuration> months;

    @Override
    public int compareTo(TrainingSession trainingSession) {
        return -this.getYear()-trainingSession.getYear();
    }

    public static Set<TrainingSession> setOf(Map<LocalDate, Double> trainingMap){
        Map<Integer, Map<Month, Double>> groupedByYear = trainingMap.entrySet().stream()
                .collect(Collectors.groupingBy(
                        e -> e.getKey().getYear(),
                        Collectors.toMap(
                                e -> e.getKey().getMonth(),
                                Map.Entry::getValue,
                                Double::sum
                        )
                ));
        return groupedByYear.entrySet().stream()
                .map(e -> {
                        TrainingSession session = new TrainingSession();
                        session.setYear(e.getKey());
                        session.setMonths(MonthDuration.setOf(e.getValue()));
                        return session;
                    }
                ).collect(Collectors.toSet());
    }

}
