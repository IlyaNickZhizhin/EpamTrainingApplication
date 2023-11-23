package org.epam.config;

import org.epam.model.gymModel.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("test")
public class TeatIninDB {

    @Value("${spring.liquibase.enabled.test}")
    private boolean enabled;

    @Autowired
    SessionFactory sessionFactory;

    @Bean
    @Profile("test")
    void initDatabase() {
        if (!enabled) {
            List<TrainingType.TrainingName> list =  TrainingType.TrainingName.getTrainingNames();
            Session session = sessionFactory.openSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                for (TrainingType.TrainingName trainingName : list) {
                    TrainingType trainingType = new TrainingType();
                    trainingType.setTrainingName(trainingName);
                    session.persist(trainingType);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                System.out.println("Error while loading data");
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }
}
