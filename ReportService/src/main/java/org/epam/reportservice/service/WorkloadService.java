package org.epam.reportservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.reportservice.dto.TrainerWorkloadRequest;
import org.epam.reportservice.dto.TrainerWorkloadResponse;
import org.epam.reportservice.model.TrainerKey;
import org.epam.reportservice.model.TrainingSession;
import org.epam.reportservice.repository.WorkloadStorage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkloadService {

    private final WorkloadStorage storage;

    public TrainerWorkloadResponse addWorkload(TrainerWorkloadRequest request) {
        log.info("WorkloadService.change(TrainerWorkloadRequest request) starts ADD workload for trainer " +
                request.getFirstName() + " " + request.getLastName().charAt(0) + "*** training on"
                +  request.getTrainingDate());
        TrainerKey key = TrainerKey.of(request);
        return TrainerWorkloadResponse.of(key, storage.addOrUpdate(key, TrainingSession.of(request)));
    }

    public TrainerWorkloadResponse deleteWorkload(TrainerWorkloadRequest request) {
        log.info("WorkloadService.change(TrainerWorkloadRequest request) starts DELETE workload or trainer " +
                                request.getFirstName() + request.getLastName().charAt(0) + "*** training on " +
                                request.getTrainingDate());
        TrainerKey key = TrainerKey.of(request);
        return TrainerWorkloadResponse.of(key, storage.deleteOrUpdate(key, TrainingSession.of(request)));
    }
}
