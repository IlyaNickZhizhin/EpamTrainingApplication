package org.epam.reportservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.service.WorkloadService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkloadReceiver {

    private final WorkloadService workloadService;

    @JmsListener(destination = "addTrainingRequestQueue", containerFactory = "defaultJmsListenerContainerFactory")
    public ReportTrainerWorkloadResponse receiveAddMessage(TrainerWorkloadRequest request){
        log.info("Adding workload of trainer{} {}***", request.getFirstName(), request.getLastName().charAt(0));
        try {
            return workloadService.addWorkload(request);
        } catch (Exception e){
            return new ReportTrainerWorkloadResponse();
        }
    }

    @JmsListener(destination = "deleteTrainingRequestQueue", containerFactory = "defaultJmsListenerContainerFactory")
    public ReportTrainerWorkloadResponse receiveDeleteMessage(TrainerWorkloadRequest request){
        log.info("Deleting workload of trainer{} {}***", request.getFirstName(), request.getLastName().charAt(0));
        try {
            return workloadService.deleteWorkload(request);
        } catch (Exception e){
            return new ReportTrainerWorkloadResponse();
        }
    }

}
