package org.epam.reportservice.config;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.bson.Document;
import org.epam.reportservice.model.Workload;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;

@Configuration
@Profile("without")
public class NoMongoConfig extends AbstractMongoClientConfiguration {

    @Override
    public MongoClient mongoClient(){
        MongoServer server = new MongoServer(new MemoryBackend());
        InetSocketAddress serverAddress = server.bind();
        String mongoUri = "mongodb://" + serverAddress.getHostName() + ":" + serverAddress.getPort();
        return MongoClients.create(mongoUri);
    }

    @Override
    protected String getDatabaseName() {
        return "gymWorkloads";
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

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost?broker.persistent=false");
    }

    @Bean
    public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrency("1-10");
        return factory;
    }

}
