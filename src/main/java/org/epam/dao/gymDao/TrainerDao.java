package org.epam.dao.gymDao;

import org.epam.storageInFile.Storage;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.Trainer;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDao extends GymDaoStorage<Trainer> {

    private static final String NAMESPACE = Trainer.class.getName();

    public TrainerDao(Storage storage) {
        super(storage);
        super.namespace = NAMESPACE;
    }

    @Override
    public void update(int id, Model model) {
        Trainer trainer = (Trainer) model;
        Trainer trainerToUpdate = (Trainer) models.get(namespace).get(id);
        trainerToUpdate.setUserId(trainer.getUserId());
        trainerToUpdate.setSpecialization(trainer.getSpecialization());
        save(trainerToUpdate);
    }

}