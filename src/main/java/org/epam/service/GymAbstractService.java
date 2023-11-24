package org.epam.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.GymAbstractDao;
import org.epam.dao.UserDaoImpl;
import org.epam.exceptions.VerificationException;
import org.epam.mapper.GymMapper;
import org.epam.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public abstract class GymAbstractService<M> {

    @Autowired
    protected GymAbstractDao<M> gymDao;

    @Autowired
    protected UserDaoImpl userDao;

    @Autowired
    protected PasswordChecker passwordChecker;

    @Autowired
    protected GymMapper gymMapper;

    protected M update(int id, M updatedModel) {
        log.info("Updating " + getModelName() + " with id: " + id);
        try {
            gymDao.update(id, updatedModel);
        } catch (Exception e) {
            log.error("Error updating " + getModelName() + " with id: " + id, e);
            throw e;
        }
        return updatedModel;
    }


    protected void delete(int id) {
        log.info("Deleting " + getModelName() + " with id " + id);
        try {
            gymDao.delete(id);
        } catch (Exception e) {
            log.error("Error deleting " + getModelName() + " with id " + id, e);
            throw e;
        }
    }

    protected M select(int id) {
        log.info("Selecting " + getModelName() + " with id " + id);
        try {
            return gymDao.get(id);
        } catch (Exception e) {
            log.error("Error selecting " + getModelName() + " with id " + id, e);
            throw e;
        }
    }

    List<M> selectAll() {
        log.info("Selecting all " + getModelName() + "s");
        try {
            return gymDao.getAll();
        } catch (Exception e) {
            log.error("Error selecting all " + getModelName() + "s", e);
            throw e;
        }
    }


    protected M selectByUser(String username) {
        log.info("Selecting " + getModelName() + " with username " + username);
        try {
            return gymDao.getModelByUserId(selectUserByUsername(username).getId());
        } catch (Exception e) {
            log.error("Error selecting " + getModelName() + " with username " + username, e);
            throw e;
        }
    }

    final void verify(String username, String password, User user) throws VerificationException {
        passwordChecker.checkPassword(username, password, user);
    }

    User selectUserByUsername(String username) {
        log.info("Selecting User with username " + username);
        return userDao.getByUsername(username);
    }

    protected abstract String getModelName();

}
