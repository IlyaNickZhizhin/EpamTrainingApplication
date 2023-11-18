package org.epam.config;

import org.epam.model.gymModel.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LoadData {

    @Autowired
    SessionFactory sessionFactory;

    @Bean
    void initDatabase() {
        List<TrainingType.TrainingName> list =  TrainingType.TrainingName.gelList();
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            for (TrainingType.TrainingName trainingName : list) {
                session.persist(new TrainingType(trainingName));
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
