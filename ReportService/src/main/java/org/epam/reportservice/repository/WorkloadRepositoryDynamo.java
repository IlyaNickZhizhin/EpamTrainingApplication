package org.epam.reportservice.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.reportservice.model.WorkloadDynamo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Profile("cloud")
@Repository
@RequiredArgsConstructor
@Slf4j
public class WorkloadRepositoryDynamo{

    private final DynamoDBMapper dynamoDBMapper;

    public WorkloadDynamo save(WorkloadDynamo workload) {
        log.info("Start saving workload for trainer {}", workload.getFirstName());
        dynamoDBMapper.save(workload);
        log.info("Finish saving workload for trainer {}", workload.getFirstName());
        return workload;
    }

    public Optional<WorkloadDynamo> findById(String id, String active) {
        log.info("Start finding workload of trainer {}", (id.charAt(0)+"***"));
        Optional<WorkloadDynamo> result = Optional.ofNullable(dynamoDBMapper.load(WorkloadDynamo.class, id, active));
        log.info("Start finding workload of trainer {}", (id.charAt(0)+"***"));
        return result;
    }
}
