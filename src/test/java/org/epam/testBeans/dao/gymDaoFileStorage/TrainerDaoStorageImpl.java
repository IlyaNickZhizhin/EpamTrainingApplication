package org.epam.testBeans.dao.gymDaoFileStorage;

import org.epam.model.gymModel.Trainer;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDaoStorageImpl extends GymDaoStorage<Trainer> {

    private static final String NAMESPACE = Trainer.class.getName();

    public TrainerDaoStorageImpl() {
        super.namespace = NAMESPACE;
    }

    @Override
    public void update(int id, Trainer trainer) {
        Trainer trainerToUpdate = storage.getGymModels().get(namespace).get(id);
        trainerToUpdate.setUser(trainer.getUser());
        trainerToUpdate.setSpecialization(trainer.getSpecialization());
        save(trainerToUpdate);
    }

}