package org.epam.reportservice.repository.mongo;

import org.epam.reportservice.model.Workload;
import org.epam.reportservice.repository.CommonRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!cloud")
public interface WorkloadRepositoryMongo extends CommonRepository<Workload, String>, MongoRepository<Workload, String> {
}
