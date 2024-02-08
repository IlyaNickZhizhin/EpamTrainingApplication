package org.epam.reportservice.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.SortedSet;
import java.util.TreeSet;

@Data
public class TrainingSession implements Comparable<TrainingSession>{
    private int year;
    private SortedSet<MonthDuration> months;

    public static TrainingSession of(LocalDate date, double duration){
        TrainingSession trainingSession = new TrainingSession();
        trainingSession.setYear(date.getYear());
        SortedSet<MonthDuration> sortedSet = new TreeSet<>();
        sortedSet.add(MonthDuration.of(date, duration));
        trainingSession.setMonths(sortedSet);
        return trainingSession;
    }

    @Override
    public int compareTo(TrainingSession trainingSession) {
        return -this.getYear()-trainingSession.getYear();
    }
}