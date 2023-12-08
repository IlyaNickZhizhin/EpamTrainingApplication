package org.epam.testBeans;

import jakarta.persistence.EntityManager;
import org.epam.model.gymModel.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@Profile("test")
@PropertySource("classpath:application-test.properties")
public class TestInitDB {

    @Autowired
    EntityManager entityManager;

    @Value("${spring.liquibase.enabled}")
    private boolean enabled;

    @Bean
    @Profile("test")
    void initDatabase() {
        if (!enabled) {
            List<TrainingType.TrainingName> list =  TrainingType.TrainingName.getTrainingNames();
            try {
                entityManager.getTransaction().begin();
                for (TrainingType.TrainingName trainingName : list) {
                    TrainingType trainingType = new TrainingType();
                    trainingType.setTrainingName(trainingName);
                    entityManager.persist(trainingType);
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Error while loading data");
                e.printStackTrace();
            }
        }
    }
}
