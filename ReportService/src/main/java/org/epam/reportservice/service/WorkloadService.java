package org.epam.reportservice.service;

import lombok.RequiredArgsConstructor;
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
public class WorkloadService {

    private final WorkloadStorage storage;
    private final ReportMapper mapper;

    public TrainerWorkloadResponse change(TrainerWorkloadRequest request) {
        return request.getActionType().equals(ActionType.ADD) ? addWorkload(request) : deleteWorkload(request);
    }

    private TrainerWorkloadResponse deleteWorkload(TrainerWorkloadRequest request) {
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
        return response;
    }

    private TrainerWorkloadResponse addWorkload(TrainerWorkloadRequest request) {
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
        return response;
    }
}
