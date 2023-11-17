package org.epam.dao;

import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.Training;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TraineeDaoImpl extends GymAbstractDaoImpl<Trainee>{

    @Override
    public void update(int id, Trainee updatedTrainee) {
        Trainee trainee = get(id);
        if (trainee != null) {
            trainee.setUser(updatedTrainee.getUser());
            trainee.setAddress(updatedTrainee.getAddress());
            trainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
            sessionFactory.getCurrentSession().merge(trainee);
        }
    }

    public List<Trainer> updateTrainersList(int id, Trainee traineeForUpdateList) {
        return sessionFactory.getCurrentSession().createQuery("from Training where trainee = :trainee", Training.class)
                .setParameter("trainee", traineeForUpdateList)
                .getResultStream().map(Training::getTrainer).collect(Collectors.toList());
    }
}
