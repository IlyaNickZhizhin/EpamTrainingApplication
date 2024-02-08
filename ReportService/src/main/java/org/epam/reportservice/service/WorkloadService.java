package org.epam.reportservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.common.dto.TrainingSession;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.model.TrainerKey;
import org.epam.reportservice.repository.WorkloadStorage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkloadService {

    private final WorkloadStorage storage;

    public ReportTrainerWorkloadResponse addWorkload(TrainerWorkloadRequest request) {
        log.info("WorkloadService.change(ReportTrainerWorkloadRequest request) starts ADD workload for trainer {} {}***" +
                " training on{}", request.getFirstName(), request.getLastName().charAt(0), request.getTrainingDate());
        TrainerKey key = TrainerKey.of(request);
        return ReportTrainerWorkloadResponse.of(key, storage.addWorkload(key, TrainingSession.of(request)));
    }

    public ReportTrainerWorkloadResponse deleteWorkload(TrainerWorkloadRequest request) {
        log.info("{}.{}starts DELETE workload of trainer {}{}*** training on {}",
                this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[2].getMethodName(),
                request.getFirstName(), request.getLastName().charAt(0), request.getTrainingDate());
        TrainerKey key = TrainerKey.of(request);
        return ReportTrainerWorkloadResponse.of(key, storage.deleteWorkload(key, TrainingSession.of(request)));
    }
}
