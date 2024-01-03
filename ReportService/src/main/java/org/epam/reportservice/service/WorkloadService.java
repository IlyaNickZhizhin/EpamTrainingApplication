package org.epam.reportservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.reportservice.dto.TrainerWorkloadRequest;
import org.epam.reportservice.dto.TrainerWorkloadResponse;
import org.epam.reportservice.mapper.ReportMapper;
import org.epam.reportservice.model.ActionType;
import org.epam.reportservice.repository.WorkloadStorage;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkloadService {

    private final WorkloadStorage storage;
    private final ReportMapper mapper;

    public TrainerWorkloadResponse change(TrainerWorkloadRequest request) {
        log.info("WorkloadService.change(TrainerWorkloadRequest request) starts work");
        return request.getActionType().equals(ActionType.ADD) ? addWorkload(request) : deleteWorkload(request);
    }

    private TrainerWorkloadResponse deleteWorkload(TrainerWorkloadRequest request) {
        log.info("WorkloadService.change(TrainerWorkloadRequest request) starts DELETE workload for trainer " +
                request.getFirstName() + " " + request.getLastName().charAt(0) + "*** training on"
                +  request.getTrainingDate());
        TrainerWorkloadResponse response = mapper.RequestToResponse(request);
        if (storage.getStorage().get(request.getUsername()) == null) return response;
        if (storage.getStorage().get(request.getUsername())
                .get(Year.of(request.getTrainingDate().getYear())) == null) return response;
        if (storage.getStorage().get(request.getUsername())
                .get(Year.of(request.getTrainingDate().getYear()))
                .get(request.getTrainingDate().getMonth()) == null) return response;
        double duration = storage.getStorage().get(request.getUsername())
                .get(Year.of(request.getTrainingDate().getYear()))
                .get(request.getTrainingDate().getMonth());
        duration -= request.getDuration();
        storage.getStorage().get(request.getUsername())
                .get(Year.of(request.getTrainingDate().getYear()))
                .put(request.getTrainingDate().getMonth(), duration);
        response.setMap(storage.getStorage().get(request.getUsername()));
        log.info("WorkloadService.change(TrainerWorkloadRequest request) DELETE workload duration"
                + request.getDuration() + "for trainer " + request.getFirstName());
        return response;
    }

    private TrainerWorkloadResponse addWorkload(TrainerWorkloadRequest request) {
        log.info("WorkloadService.change(TrainerWorkloadRequest request) starts ADD workload or trainer " +
                                request.getFirstName() + request.getLastName().charAt(0) + "*** training on " +
                                request.getTrainingDate());
        Month month = request.getTrainingDate().getMonth();
        Year year = Year.of(request.getTrainingDate().getYear());
        Map<Year, Map<Month, Double>> history = storage.getStorage()
                .computeIfAbsent(request.getUsername(), k -> new HashMap<>());
        Map<Month, Double> yearHistory = history
                .computeIfAbsent(year, k -> new HashMap<>());
        double duration = yearHistory.getOrDefault(month, 0.0);
        duration += request.getDuration();
        storage.getStorage().get(request.getUsername()).get(year).put(month, duration);
        TrainerWorkloadResponse response = mapper.RequestToResponse(request);
        response.setMap(storage.getStorage().get(request.getUsername()));
        log.info("WorkloadService.change(TrainerWorkloadRequest request) ADD workload duration"
                + request.getDuration() + "for trainer " + request.getFirstName());
        return response;
    }
}
