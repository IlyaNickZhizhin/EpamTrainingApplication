package org.epam.service;

import jakarta.transaction.Transactional;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.GymAbstractDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.exceptions.ProhibitedAction;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.UserSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.List;


@Service
@Transactional
public abstract class GymAbstractService<M extends Model> {
    protected GymAbstractDaoImpl<M> gymDao;
    protected UserDaoImpl userDao;
    protected PasswordChecker passwordChecker;
    protected Class<M> modelClass;
    @Autowired
    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }
    @Autowired
    public void setPasswordChecker(PasswordChecker passwordChecker) {
        this.passwordChecker = passwordChecker;
    }

    public GymAbstractService() {
        this.modelClass = (Class<M>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
    protected final M create(String name, String surname, Object... parameters) {
        M model;
        User user;
        if (UserSetter.class.isAssignableFrom(modelClass)) {
            user = userDao.setNewUser(name, surname);
            model = createModel(user, parameters);
            ((UserSetter) model).setUser(user);
        } else {
            model = createModel(userDao.whoIsUser(name), parameters);
        }
        gymDao.save(model);
        return model;
    }

    protected abstract M createModel(Object object, Object... parameters);

    protected M update(int id, M upadatedModel) {
        gymDao.update(id, upadatedModel);
        return upadatedModel;
    }

    protected void delete(int id) {
        gymDao.delete(id);
    }

    protected M select(int id) {
        return gymDao.get(id);
    }

    protected List<M> selectAll() {
        return gymDao.getAll();
    }

    protected M selectByUsername(String username) {
        User user = userDao.getByUsername(username);
        return gymDao.getByUserId(user.getId());
    }

    // TODO нормально ли это что из TraineeService можно менять пароль и активность Trainer и наоборот?
    protected void changePassword(String username, String newPassword) {
        User user = userDao.getByUsername(username);
        user.setPassword(newPassword);
        userDao.update(user.getId(), user);
    }

    protected void changeActive(String username){
        User user = userDao.getByUsername(username);
        user.setActive(!user.isActive());
        userDao.update(user.getId(), user);
    }

    // TODO не понимаю какой из двух вариантов выше или ниже - корректный?
    //   НЕ идемпотентная операция — это действие, многократное повторение которого НЕ эквивалентно однократному.

    protected void setActive(String username, boolean isActive) {
        User user = userDao.getByUsername(username);
        if (user.isActive() != isActive) userDao.update(user.getId(), user);
        else //return;
            throw new ProhibitedAction("It is not possible to set active to "
                + isActive + " for user it is already " + user.isActive());
    }

    protected final void verify(String username, String password) throws VerificationException {
        passwordChecker.checkPassword(username, password);
    }

}
