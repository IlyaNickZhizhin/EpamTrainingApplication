package org.epam.reportservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.common.dto.MonthDuration;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.mapper.MongoToDynamoMapper;
import org.epam.reportservice.model.TrainingSession;
import org.epam.reportservice.model.Workload;
import org.epam.reportservice.model.WorkloadDynamo;
import org.epam.reportservice.repository.WorkloadRepositoryDynamo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("cloud")
public class WorkloadCloudService {

    private final WorkloadRepositoryDynamo repository;
    private final MongoToDynamoMapper mapper;


    public ReportTrainerWorkloadResponse addWorkload(TrainerWorkloadRequest request) {
        log.info("WorkloadService.change(ReportTrainerWorkloadRequest request) starts ADD workload for trainer {} {}***" +
                " training on {}", request.getFirstName(), request.getLastName().charAt(0), request.getTrainingDate());
        WorkloadDynamo workload = repository
                .findById(request.getUsername(), String.valueOf(request.isActive()))
                .orElseGet(()-> mapper.mongoToDynamo(Workload.of(request)));
        log.info("WorkloadService.change(ReportTrainerWorkloadRequest request) found workload of trainer {} {}*** training on {}",
                request.getFirstName(), request.getLastName().charAt(0), request.getTrainingDate());
        getOrAddTrainingSession(workload.getTrainingSessions(), request.getTrainingDate(), request.getDuration());
        log.info("WorkloadService.change(ReportTrainerWorkloadRequest request) finished ADDing workload of trainer {} {}*** training on {}",
                request.getFirstName(), request.getLastName().charAt(0), request.getTrainingDate());
        return ReportTrainerWorkloadResponse.of(mapper.dynamoToMongo(repository.save(workload)));
    }


    public ReportTrainerWorkloadResponse deleteWorkload(TrainerWorkloadRequest request) {
        log.info("{}.{} starts DELETE workload of trainer {} {}*** training on {}",
                this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName(),
                request.getFirstName(), request.getLastName().charAt(0), request.getTrainingDate());
        WorkloadDynamo workload = repository.findById(request.getUsername(), String.valueOf(request.isActive())).orElseThrow(
                () -> new NoSuchElementException(
                        "Trainer " + request.getFirstName() + " " + request.getLastName().charAt(0) + "***" +
                                "has no training workload")
        );
        log.info("{}.{} found workload of trainer {} {}*** training on {}",
                this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName(),
                request.getFirstName(), request.getLastName().charAt(0), request.getTrainingDate());
        workload.setTrainingSessions(getAndDeleteFromTrainingSession(workload.getTrainingSessions(), request.getTrainingDate(), request.getDuration()));
        log.info("{}.{} finished DELETing workload of trainer {} {}*** training on {}",
                this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName(),
                request.getFirstName(), request.getLastName().charAt(0), request.getTrainingDate());
        return ReportTrainerWorkloadResponse.of(mapper.dynamoToMongo(repository.save(workload)));
    }


    private void getOrAddMonthDuration(Set<MonthDuration> monthDurations, LocalDate date, double duration){
        log.info("STARTED getOrAddMonthDuration(Set<MonthDuration>, {}, {})", date.getMonth(), duration);
        for (MonthDuration monthDuration : monthDurations){
            if (monthDuration.getMonth().equals(date.getMonth())) {
                monthDuration.setDuration(monthDuration.getDuration()+duration);
                log.info("SETTING getOrAddMonthDuration(Set<MonthDuration>, {}, {}) FINISHED", date.getMonth(), duration);
                return;
            }
        }
        log.info("getOrAddMonthDuration(Set<MonthDuration>, {}, {}) FINISHED", date.getMonth(), duration);
        monthDurations.add(MonthDuration.of(date.getMonth(), duration));
    }

    private Set<MonthDuration> getAndDeleteMonthDuration(Set<MonthDuration> monthDurations, LocalDate date, double duration){
        log.info("STARTED getAndDeleteMonthDuration(Set<MonthDuration>, {}, {})", date.getMonth(), duration);
        for (MonthDuration monthDuration : monthDurations){
            if (monthDuration.getMonth().equals(date.getMonth())) {
                monthDuration.setDuration(monthDuration.getDuration()-duration);
                log.info("getAndDeleteMonthDuration(Set<MonthDuration>, {}, {}) FINISHED", date.getMonth(), duration);
                return monthDurations;
            }
        }
        throw new NoSuchElementException("There are no trainings at that month: " + date);
    }

    private void getOrAddTrainingSession(Set<TrainingSession> trainingSessions, LocalDate date, double duration){
        log.info("STARTED getOrAddTrainingSession(Set<TrainingSession>, {}, {})", date.getMonth(), duration);
        for (TrainingSession trainingSession : trainingSessions){
            if (trainingSession.getYear() == date.getYear()){
                getOrAddMonthDuration(trainingSession.getMonths(), date, duration);
                log.info("SETTING getOrAddTrainingSession(Set<TrainingSession>, {}, {}) FINISHED", date.getMonth(), duration);
                return;
            }
        }
        log.info("getOrAddTrainingSession(Set<TrainingSession>, {}, {}) FINISHED", date.getMonth(), duration);
        trainingSessions.add(TrainingSession.of(date, duration));
    }

    private Set<TrainingSession> getAndDeleteFromTrainingSession(Set<TrainingSession> trainingSessions, LocalDate date, double duration){
        log.info("STARTED getAndDeleteFromTrainingSession(Set<TrainingSession>, {}, {})", date.getMonth(), duration);
        for (TrainingSession trainingSession : trainingSessions){
            if (trainingSession.getYear()==(date.getYear())){
                trainingSession.setMonths(getAndDeleteMonthDuration(trainingSession.getMonths(), date, duration));
                log.info("getAndDeleteFromTrainingSession(Set<TrainingSession>, {}, {}) FINISHED", date.getMonth(), duration);
                return trainingSessions;
            }
        }
        throw new NoSuchElementException("There are no trainings at that date: " + date);
    }
}
