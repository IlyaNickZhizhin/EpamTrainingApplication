package org.epam.service;

import org.epam.dao.TraineeDaoImpl;
import org.epam.exceptions.InvaildDeveloperException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TraineeService extends GymAbstractService<Trainee> {

    @Autowired
    public void setTraineeDao(TraineeDaoImpl traineeDao) {
        super.gymDao = traineeDao;
    }

    //TODO я не понял зачем этот метод и сделал его НЕ верно.
    public List<Trainer> updateTrainersList(int id, Trainee traineeForUpdateList) {
        return ((TraineeDaoImpl) super.gymDao).updateTrainersList(id, traineeForUpdateList);
    }

    public Trainee create(String firstName, String lastName) {
        return super.create(firstName, lastName);
    }

    public Trainee create(String firstName, String lastName, String address) {
        return super.create(firstName, lastName, address);
    }
    public Trainee create(String firstName, String lastName, LocalDate dateOfBirth) {
        return super.create(firstName, lastName, dateOfBirth);
    }
    public Trainee create(String firstName, String lastName, String address, LocalDate dateOfBirth) {
        return super.create(firstName, lastName, address, dateOfBirth);
    }

    @Override
    protected Trainee createModel(Object user, Object... parameters) {
        Trainee trainee = new Trainee();
        switch (parameters.length)
        {
            case 0:
                break;
            case 1:
                if (parameters[0] instanceof String) {
                    trainee.setAddress((String) parameters[0]);
                } else {
                    trainee.setDateOfBirth((LocalDate) parameters[0]);
                }
                break;
            case 2:
                trainee.setAddress((String) parameters[0]);
                trainee.setDateOfBirth((LocalDate) parameters[1]);
                break;
            default:
                throw new InvaildDeveloperException("It is not possible to be here!!!");
        }
        trainee.setUser((User) user);
        return trainee;
    }

    public Trainee update(String oldUsername, String oldPassword, int id, Trainee upadatedModel) throws VerificationException {
        super.verify(oldUsername, oldPassword);
        return super.update(id, upadatedModel);
    }

    public void delete(String username, String password, int id) throws VerificationException {
        super.verify(username, password);
        super.delete(id);
    }

    public Trainee select(String username, String password, int id) throws VerificationException {
        super.verify(username, password);
        return super.select(id);
    }

    public Trainee selectByUsername(String username, String password) throws VerificationException {
        super.verify(username, password);
        return super.selectByUsername(username);
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws VerificationException {
        super.verify(username, oldPassword);
        super.changePassword(username, newPassword);
    }

    public void changeActive(String username, String password) throws VerificationException {
        super.verify(username, password);
        super.changeActive(username);
    }

    public void setActive(String username, String password, boolean isActive) throws VerificationException {
        super.verify(username, password);
        super.setActive(username, isActive);
    }
}
