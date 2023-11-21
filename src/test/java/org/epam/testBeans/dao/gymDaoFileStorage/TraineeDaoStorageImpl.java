package org.epam.testBeans.dao.gymDaoFileStorage;

import org.epam.model.gymModel.Trainee;
import org.epam.testBeans.storageInFile.Storage;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeDaoStorageImpl extends GymDaoStorage<Trainee> {

    private static final String NAMESPACE = Trainee.class.getName();

    public TraineeDaoStorageImpl(Storage<Trainee> storage) {
        super(storage);
        super.namespace = NAMESPACE;
    }

    @Override
    public void update(int id, Trainee trainee) {
        Trainee traineeToUpdate = storage.getGymModels().get(NAMESPACE).get(id);
        traineeToUpdate.setUser(trainee.getUser());
        traineeToUpdate.setAddress(trainee.getAddress());
        traineeToUpdate.setAddress(trainee.getAddress());
        traineeToUpdate.setDateOfBirth(trainee.getDateOfBirth());
        save(traineeToUpdate);
    }
}
