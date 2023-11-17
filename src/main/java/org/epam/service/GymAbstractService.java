package org.epam.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.GymAbstractDaoImpl;
import org.epam.dao.UserDaoImpl;
import org.epam.model.User;
import org.epam.model.gymModel.Model;
import org.epam.model.gymModel.UserSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public abstract class GymAbstractService<M extends Model> {

        protected GymAbstractDaoImpl<M> gymDao;

        protected UserDaoImpl userDao;

        protected PasswordChecker passwordChecker;

        @Autowired
        public void setUserDao(UserDaoImpl userDao) {
            this.userDao = userDao;
        }

        @Autowired
        public void setPasswordChecker(PasswordChecker passwordChecker) {
            this.passwordChecker = passwordChecker;
        }

        @Transactional
        public M create(String firstName, String lastName) {
            User user = userDao.setNewUser(firstName, lastName);
            M model = createUserSetter(user);
            if (model instanceof UserSetter) {
                ((UserSetter) model).setUser(user);
            }
            gymDao.save(model);
            return model;
        }

        @Transactional
        public M update(int id, M upadatedModel, String oldUsername, String oldPassword) throws AccessDeniedException {
            if (passwordChecker.checkPassword(oldUsername, oldPassword)) {
                gymDao.update(id, upadatedModel);
                return upadatedModel;
            }
            throw new AccessDeniedException("Wrong password");
        }

        @Transactional
        public void delete(int id, String username, String password) throws AccessDeniedException {
            if (passwordChecker.checkPassword(username, password)) {
                gymDao.delete(id);
            }
            throw new AccessDeniedException("Wrong password");
        }

        public M select(int id, String username, String password) throws AccessDeniedException {
            if (passwordChecker.checkPassword(username, password)) {
                return gymDao.get(id);
            }
            throw new AccessDeniedException("Wrong password");
        }

        public List<M> selectAll() {
            return gymDao.getAll();
        }

        public M selectByUsername(String username, String password) throws AccessDeniedException {
            if (passwordChecker.checkPassword(username, password)) {
                User user = userDao.get(username);
                return gymDao.getByUserId(user.getId());
            }
            throw new AccessDeniedException("Wrong password");
        }

        public void changePassword(String username, String password) throws AccessDeniedException {
            if (passwordChecker.checkPassword(username, password)) {
                User user = userDao.get(username);
                user.setPassword(password);
                userDao.update(user.getId(), user);
            }
            throw new AccessDeniedException("Wrong password");
        }

        public void setActive(boolean isActive, String username, String password) throws AccessDeniedException {
            if (passwordChecker.checkPassword(username, password)) {
                User user = userDao.get(username);
                user.setActive(isActive);
                userDao.update(user.getId(), user);
            }
            throw new AccessDeniedException("Wrong password");
        }

        protected abstract M createUserSetter(User user);
}
