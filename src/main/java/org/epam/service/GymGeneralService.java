package org.epam.service;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.GymAbstractDao;
import org.epam.dao.UserDaoImpl;
import org.epam.exceptions.ProhibitedActionException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GymGeneralService<M> {
    private final GymAbstractDao<M> gymDao;
    private final UserDaoImpl userDao;
    private final PasswordChecker passwordChecker;


    M update(int id, M updatedModel) {
        log.info("Updating " + updatedModel.getClass().getSimpleName() + " with id: " + id);
        try {
            updatedModel = gymDao.update(id, updatedModel);
        } catch (Exception e) {
            log.error("Error updating " + updatedModel.getClass().getSimpleName() + " with id: " + id, e);
            throw e;
        }
        return updatedModel;
    }


    void delete(int id) {
        log.info("Deleting entity with id " + id);
        try {
            gymDao.delete(id);
        } catch (Exception e) {
            log.error("Error deleting entity with id " + id, e);
            throw e;
        }
    }
    M select(int id) {
        log.info("Selecting model in superservice with id " + id);
        try {
            return gymDao.get(id);
        } catch (Exception e) {
            log.error("Error selecting in superservice with id " + id, e);
            throw e;
        }
    }

    M selectByUsername(String username) {
        log.info("Selecting in superservice with username " + username);
        try {
            return gymDao.getModelByUser(selectUserByUsername(username));
        } catch (Exception e) {
            log.error("Error selecting in superservice with username " + username, e);
            throw e;
        }
    }
    List<M> selectAll() {
        log.info("Selecting all models in super-service");
        try {
            return gymDao.getAll();
        } catch (Exception e) {
            log.error("Error selecting all models in super-service", e);
            throw e;
        }
    }

    final void verify(String username, String password, User user) throws VerificationException {
        passwordChecker.checkPassword(username, password, user);
    }


    final void changePassword(User user, @NotBlank String newPassword) {
        if (!user.getPassword().equals(newPassword)) {
            user.setPassword(newPassword);
            userDao.update(user.getId(), user);
            log.info("Changing password for " + user.getUsername() + "in userservice");
        } else {
            log.info("Changing password for " + user.getUsername() + " failed it is allready" + newPassword);
            throw new ProhibitedActionException("New password is the same as old");
        }
    }
    final void setActive(User user, boolean isActive){
        if (user.isActive() != isActive) {
            user.setActive(isActive);
            userDao.update(user.getId(), user);
            log.info("Setting active status for " + user.getUsername() + " to " + isActive);
        }
        log.info("Setting active status for " + user.getUsername() + " failed it is allready" + isActive);
    }


    final User selectUserByUsername(String username) {
        log.info("Selecting User with username " + username);
        return userDao.getByUsername(username);
    }

}
