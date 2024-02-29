package org.epam.reportservice.repository;

import org.epam.reportservice.model.Workload;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkloadRepository extends MongoRepository<Workload, String> {
}
