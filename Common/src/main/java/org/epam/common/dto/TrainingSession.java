package org.epam.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

@Data
public class TrainingSession implements Comparable<TrainingSession>, Serializable {
    private Year year;
    private Month month;
    private double duration;

    public static TrainingSession of(LocalDate date, double duration) {
        TrainingSession trainingSession = new TrainingSession();
        trainingSession.year = Year.of(date.getYear());
        trainingSession.month = date.getMonth();
        trainingSession.duration = duration;
        return trainingSession;
    }

    public static TrainingSession of(TrainerWorkloadRequest request) {
        TrainingSession trainingSession = new TrainingSession();
        trainingSession.year = Year.of(request.getTrainingDate().getYear());
        trainingSession.month = request.getTrainingDate().getMonth();
        trainingSession.duration = request.getDuration();
        return trainingSession;
    }

    @Override
    public int compareTo(TrainingSession trainingSession) {
        if (trainingSession.getYear().isAfter(this.year)) return 1;
        else { if (trainingSession.getMonth().equals(this.month)) return 0;
        else return -1;
        }
    }
}
