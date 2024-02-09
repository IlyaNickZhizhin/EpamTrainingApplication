package org.epam.reportservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.model.MonthDuration;
import org.epam.reportservice.model.TrainingSession;
import org.epam.reportservice.model.Workload;
import org.epam.reportservice.repository.WorkloadRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.SortedSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkloadMongoService {
    private final WorkloadRepository repository;


    public ReportTrainerWorkloadResponse addWorkload(TrainerWorkloadRequest request) {
        log.info("WorkloadService.change(ReportTrainerWorkloadRequest request) starts ADD workload for trainer {} {}***" +
                " training on{}", request.getFirstName(), request.getLastName().charAt(0), request.getTrainingDate());
        Workload workload = repository.findById(request.getUsername()).orElseGet(()-> Workload.of(request));
        getOrAddTrainingSession(workload.getTrainingSessions(), request.getTrainingDate(), request.getDuration());
        return ReportTrainerWorkloadResponse.of(repository.save(workload));
    }


    public ReportTrainerWorkloadResponse deleteWorkload(TrainerWorkloadRequest request) {
        log.info("{}.{} starts DELETE workload of trainer {} {}*** training on {}",
                this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName(),
                request.getFirstName(), request.getLastName().charAt(0), request.getTrainingDate());
        Workload workload = repository.findById(request.getUsername()).orElseThrow(
                () -> new NoSuchElementException(
                        "Trainer " + request.getFirstName() + " " + request.getLastName().charAt(0) + "***" +
                "has no training workload")
        );
        workload.setTrainingSessions(getAndDeleteFromTrainingSession(workload.getTrainingSessions(), request.getTrainingDate(), request.getDuration()));
        return ReportTrainerWorkloadResponse.of(repository.save(workload));
    }


    private void getOrAddMonthDuration(SortedSet<MonthDuration> monthDurations, LocalDate date, double duration){
        for (MonthDuration monthDuration : monthDurations){
            if (monthDuration.getMonth().equals(date.getMonth())) {
                monthDuration.setDuration(monthDuration.getDuration()+duration);
                return;
            }
        }
        monthDurations.add(MonthDuration.of(date, duration));
    }

    private SortedSet<MonthDuration> getAndDeleteMonthDuration(SortedSet<MonthDuration> monthDurations, LocalDate date, double duration){
        for (MonthDuration monthDuration : monthDurations){
            if (monthDuration.getMonth().equals(date.getMonth())) {
                monthDuration.setDuration(monthDuration.getDuration()-duration);
                return monthDurations;
            }
        }
        throw new NoSuchElementException("There are no trainings at that month: " + date);
    }

    private void getOrAddTrainingSession(SortedSet<TrainingSession> trainingSessions, LocalDate date, double duration){
        for (TrainingSession trainingSession : trainingSessions){
            if (trainingSession.getYear() == date.getYear()){
                getOrAddMonthDuration(trainingSession.getMonths(), date, duration);
                return;
            }
        }
        trainingSessions.add(TrainingSession.of(date, duration));
    }

    private SortedSet<TrainingSession> getAndDeleteFromTrainingSession(SortedSet<TrainingSession> trainingSessions, LocalDate date, double duration){
        for (TrainingSession trainingSession : trainingSessions){
            if (trainingSession.getYear()==(date.getYear())){
                trainingSession.setMonths(getAndDeleteMonthDuration(trainingSession.getMonths(), date, duration));
                return trainingSessions;
            }
        }
        throw new NoSuchElementException("There are no trainings at that date: " + date);
    }
}
