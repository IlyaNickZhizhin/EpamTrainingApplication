package org.epam.reportservice.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.epam.reportservice.model.Workload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.Collection;

@Configuration
@Profile("!cloud")
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Override
    protected String getDatabaseName() {
        return "gymWorkloads";
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUri);
    }

    @Override
    public Collection<String> getMappingBasePackages() {
        return Arrays.asList("org.epam.reportservice.model");
    }


    @Bean
    public MongoTemplate getMongoTemplate(){
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        mongoTemplate.indexOps(Workload.class).ensureIndex(new CompoundIndexDefinition(
                new Document().append("trainingSessions.year", 1).append("trainingSessions.months.month",1)));
        return mongoTemplate;
    }

}
