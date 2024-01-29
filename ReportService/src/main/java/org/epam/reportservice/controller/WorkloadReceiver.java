package org.epam.reportservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.reportservice.dto.TrainerWorkloadRequest;
import org.epam.reportservice.dto.TrainerWorkloadResponse;
import org.epam.reportservice.service.WorkloadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkloadReceiver {

    private final WorkloadService workloadService;

    @JmsListener(destination = "addTrainingRequestQueue", containerFactory = "defaultJmsListenerContainerFactory")
    public ResponseEntity<TrainerWorkloadResponse> receiveAddMessage(TrainerWorkloadRequest request){
        log.info("Adding workload of trainer" + request.getFirstName() +
                " " + request.getLastName().charAt(0)  + "***");
        try {
            return new ResponseEntity<>(workloadService.addWorkload(request), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new TrainerWorkloadResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @JmsListener(destination = "deleteTrainingRequestQueue", containerFactory = "defaultJmsListenerContainerFactory")
    public ResponseEntity<TrainerWorkloadResponse> receiveDeleteMessage(TrainerWorkloadRequest request){
        log.info("Deleting workload of trainer" + request.getFirstName() +
                " " + request.getLastName().charAt(0)  + "***");
        try {
            return new ResponseEntity<>(workloadService.deleteWorkload(request), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new TrainerWorkloadResponse(), HttpStatus.BAD_REQUEST);
        }
    }

}
