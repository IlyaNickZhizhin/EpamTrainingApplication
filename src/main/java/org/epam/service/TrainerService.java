package org.epam.service;

import org.epam.dao.gymDao.TrainerDao;
import org.epam.dao.UserDao;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.TrainingType;
import org.epam.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {

    TrainerDao trainerDao;
    UserDao userDao;

   /* TODO - - в сервисах используй аннотацию @RequiredArgsConstructor (вроде такая)
             чтобы не писать руками конструктор каждый раз
   * не стал делать, потому что есть требование "DAO with storage bean should be inserted into services beans
   * using auto wiring. Services beans should be injected into the facade using
   * constructor-based injections. The rest of the injections should be done in a setter-based way."
   * */

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public Trainer create (TrainingType trainingType, String firstName, String lastName) {
        User user = userDao.setNewUser(firstName, lastName);
        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainingType.getName());
        trainer.setUserId(userDao.create(user));
        trainerDao.create(trainer);
        return trainer;
    }

    public Trainer update (int id, Trainer trainer){
        trainerDao.update(id, trainer);
        return trainer;
    }

    public Trainer select(int id){
        return (Trainer) trainerDao.get(id);
    }

}
