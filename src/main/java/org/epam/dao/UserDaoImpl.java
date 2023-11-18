package org.epam.dao;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.model.User;
import org.epam.model.gymModel.Trainee;
import org.epam.model.gymModel.Trainer;
import org.epam.model.gymModel.UserSetter;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Log
@Transactional
public class UserDaoImpl {

    SessionFactory factory;

    public User create(User user) {
        log.info("Creating user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        factory.getCurrentSession().persist(user);
        return user;
    }

    public void save(User user) {
        log.info("Creating user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        factory.getCurrentSession().persist(user);
    }

    public void update(int id, User user) {
        Session session = factory.getCurrentSession();
        User userToUpdate = session.get(User.class, id);
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setActive(user.isActive());
        session.merge(userToUpdate);
    }

    public void delete(int id) {
        Session session = factory.getCurrentSession();
        User user = session.get(User.class, id);
        session.delete(user);
    }

    public User get(int id) {
        Session session = factory.getCurrentSession();
        User user = session.get(User.class, id);
        return user;
    }

    public User getByUsername(String username) {
        Session session = factory.getCurrentSession();
        User user = session.createQuery("from User where username = :username", User.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
        return user;
    }

    public User setNewUser(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(UsernameGenerator.getDefaultUsername(firstName, lastName));
        user.setPassword(PasswordGenerator.getDefaultPassword());
        user.setActive(true);
        return create(user);
    }

    public UserSetter whoIsUser(String username) {
        User principal = getByUsername(username);
        Session session = factory.getCurrentSession();
        UserSetter userSetter = session.createQuery("from Trainee where user = :user", Trainee.class)
                .setParameter("user", principal)
                .getSingleResultOrNull();
        return userSetter == null ?
                session.createQuery("from Trainer where user = :user", Trainer.class)
                        .setParameter("user", principal)
                        .getSingleResult() :
                userSetter;
    }
}
