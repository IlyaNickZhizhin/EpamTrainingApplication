package org.epam.reportservice.repository;

import org.epam.reportservice.model.Workload;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

@Profile("!cloud")
public interface WorkloadRepositoryMongo extends MongoRepository<Workload, String> {
}
