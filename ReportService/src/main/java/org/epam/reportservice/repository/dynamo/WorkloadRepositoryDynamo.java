package org.epam.reportservice.repository.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import lombok.RequiredArgsConstructor;
import org.epam.reportservice.model.WorkloadDynamo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Profile("cloud")
@Repository
@RequiredArgsConstructor
public class WorkloadRepositoryDynamo{

    private final DynamoDBMapper dynamoDBMapper;

    public WorkloadDynamo save(WorkloadDynamo workload) {
        dynamoDBMapper.save(workload);
        return workload;
    }

    public Optional<WorkloadDynamo> findById(String id, String active) {
        return Optional.ofNullable(dynamoDBMapper.load(WorkloadDynamo.class, id, active));
    }
}
