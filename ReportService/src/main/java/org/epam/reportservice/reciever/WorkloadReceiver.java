package org.epam.reportservice.reciever;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.common.dto.TrainerWorkloadResponse;
import org.epam.reportservice.service.WorkloadService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkloadReceiver {

    private final WorkloadService workloadService;
    private final JmsTemplate jmsTemplate;

    @Transactional
    @JmsListener(destination = "addTrainingRequestQueue", containerFactory = "defaultJmsListenerContainerFactory")
    public TrainerWorkloadResponse receiveAddMessage(TrainerWorkloadRequest request){
        log.info("Adding workload of trainer{} {}***", request.getFirstName(), request.getLastName().charAt(0));
        try {
            return workloadService.addWorkload(request);
        } catch (Exception e){
            jmsTemplate.convertAndSend("DLQ.addTrainingRequestQueue", request);
            throw e;
        }
    }

    @Transactional
    @JmsListener(destination = "deleteTrainingRequestQueue", containerFactory = "defaultJmsListenerContainerFactory")
    public TrainerWorkloadResponse receiveDeleteMessage(TrainerWorkloadRequest request){
        log.info("Deleting workload of trainer {} {}***", request.getFirstName(), request.getLastName().charAt(0));
        try {
            return workloadService.deleteWorkload(request);
        } catch (Exception e){
            jmsTemplate.convertAndSend("DLQ.deleteTrainingRequestQueue", request);
            throw e;
        }
    }

}
