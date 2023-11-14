package org.epam.dao.gymDao;

import org.epam.storageInFile.Storage;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.Trainee;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDao extends GymDaoStorage<Trainee> {

    private static final String NAMESPACE = Trainee.class.getName();

    public TraineeDao(Storage storage) {
        super(storage);
        super.namespace = NAMESPACE;
    }

    @Override
    public void update(int id, Model model) {
        Trainee trainee = (Trainee) model;
        Trainee traineeToUpdate = (Trainee) models.get(NAMESPACE).get(id);
        traineeToUpdate.setUserId(trainee.getUserId());
        traineeToUpdate.setAddress(trainee.getAddress());
        traineeToUpdate.setAddress(trainee.getAddress());
        traineeToUpdate.setDateOfBirth(trainee.getDateOfBirth());
        save(traineeToUpdate);
    }
}
