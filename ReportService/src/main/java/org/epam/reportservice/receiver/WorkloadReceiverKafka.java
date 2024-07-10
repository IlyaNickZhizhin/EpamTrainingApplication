package org.epam.reportservice.receiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.common.dto.TrainerWorkloadRequest;
import org.epam.common.dto.TrainerWorkloadResponse;
import org.epam.reportservice.service.WorkloadService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkloadReceiverKafka {

    private final WorkloadService workloadService;
    private final KafkaTemplate<String, TrainerWorkloadRequest> kafkaTemplate;

    @KafkaListener(topics = "add-workload-topic", groupId = "add-workload-consumer")
    @Transactional
    public TrainerWorkloadResponse receiveAddMessage(TrainerWorkloadRequest request){
        log.info("Adding workload of trainer{} {}***", request.getFirstName(), request.getLastName().charAt(0));
        try {
            return workloadService.addWorkload(request);
        } catch (Exception e){
            kafkaTemplate.send("add-workload-DLQ-topic", request);
            throw e;
        }
    }

    @KafkaListener(topics = "delete-workload-topic", groupId = "delete-workload-consumer")
    public TrainerWorkloadResponse receiveDeleteMessage(TrainerWorkloadRequest request) {
        log.info("Deleting workload of trainer {} {}***", request.getFirstName(), request.getLastName().charAt(0));
        try {
            return workloadService.deleteWorkload(request);
        } catch (Exception e) {
            kafkaTemplate.send("delete-workload-DLQ-topic", request);
            throw e;
        }
    }

}
