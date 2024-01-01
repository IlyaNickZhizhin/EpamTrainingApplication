package org.epam.reportservice.service;

import lombok.RequiredArgsConstructor;
import org.epam.reportservice.dto.TrainerWorkloadRequest;
import org.epam.reportservice.dto.TrainerWorkloadResponse;
import org.epam.reportservice.mapper.ReportMapper;
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
