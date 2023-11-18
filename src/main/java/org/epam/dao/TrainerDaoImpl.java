package org.epam.dao;

import jakarta.transaction.Transactional;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class TrainerDaoImpl extends GymAbstractDaoImpl<Trainer>{


    @Override
    public void update(int id, Trainer updatedTrainer) {
        Trainer trainer = get(id);
        if (trainer != null) {
            trainer.setUser(updatedTrainer.getUser());
            trainer.setSpecialization(updatedTrainer.getSpecialization());
            sessionFactory.getCurrentSession().merge(trainer);
        }
    }

    public List<Trainer> getAllTrainersAvalibleForTrainee(Trainee trainee, List<Trainer> trainers) {
        System.out.println("All trainers: \n" + trainers);
        System.out.println("All active trainers: \n" + trainers.stream().filter(er -> er.getUser().isActive()).collect(Collectors.toList()));
        System.out.println("Trainers in list: \n" + sessionFactory.getCurrentSession()
                .createQuery("from Training where trainee = :trainee", Training.class)
                .setParameter("trainee", trainee)
                .getResultStream().map(Training::getTrainer).collect(Collectors.toList()));
        //return from list of All trainers
        return trainers.stream()
                //only active trainers
                .filter(er -> er.getUser().isActive()
                //trainers who are not in trainee's training list
                && !(sessionFactory.getCurrentSession()
                    .createQuery("from Training where trainee = :trainee", Training.class)
                    .setParameter("trainee", trainee)
                    .getResultStream().map(Training::getTrainer).collect(Collectors.toList()))
                .contains(er)).collect(Collectors.toList());
    }
}
