package org.epam.reportservice.model;

import java.time.LocalDate;
import java.util.SortedSet;
import java.util.TreeSet;

public class TrainingSession extends org.epam.common.dto.TrainingSession {
    public static TrainingSession of(LocalDate date, double duration){
        TrainingSession trainingSession = new TrainingSession();
        trainingSession.setYear(date.getYear());
        SortedSet<org.epam.common.dto.MonthDuration> sortedSet = new TreeSet<>();
        sortedSet.add(MonthDuration.of(date, duration));
        trainingSession.setMonths(sortedSet);
        return trainingSession;
    }
}