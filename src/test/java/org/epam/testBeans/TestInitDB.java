package org.epam.testBeans;

import org.epam.model.gymModel.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
    SessionFactory sessionFactory;

    @Value("${spring.liquibase.enabled}")
    private boolean enabled;


    @Bean
    @Profile("test")
    void initDatabase() {
        if (!enabled) {
            List<TrainingType.TrainingName> list =  TrainingType.TrainingName.getTrainingNames();
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = null;
                transaction = session.beginTransaction();
                for (TrainingType.TrainingName trainingName : list) {
                    TrainingType trainingType = new TrainingType();
                    trainingType.setTrainingName(trainingName);
                    session.persist(trainingType);
                }
                transaction.commit();
            } catch (Exception e) {
                System.out.println("Error while loading data");
                e.printStackTrace();
            }
        }
    }
}
