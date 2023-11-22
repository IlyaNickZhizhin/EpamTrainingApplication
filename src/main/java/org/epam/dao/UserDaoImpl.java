package org.epam.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDaoImpl {

    private final SessionFactory factory;
    private final PasswordGenerator passwordGenerator;
    UsernameGenerator usernameGenerator = new UsernameGenerator(this);


    public User create(User user) {
        log.info("Creating new user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        save(user);
        return user;
    }

    public void save(User user) {
        log.info("Saving user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        factory.getCurrentSession().persist(user);
    }

    public void update(int id, User user) {
        log.info("Updating user with id: " + id);
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
        log.info("Deleting user with id: " + id);
        Session session = factory.getCurrentSession();
        User user = session.get(User.class, id);
        session.remove(user);
    }

    public User get(int id) {
        log.info("Getting user with id: " + id);
        Session session = factory.getCurrentSession();
        try {
            return session.get(User.class, id);
        } catch (Exception e) {
            log.error("Error getting user with id: " + id, e);
            throw new ResourceNotFoundException(User.class.getSimpleName(), id);
        }
    }

    public User getByUsername(String username) {
        Session session = factory.getCurrentSession();
        log.info("Getting user with username: " + username);
        try {
            return session.createQuery("from User where username = :username", User.class)
                        .setParameter("username", username)
                        .getSingleResult();
        } catch (Exception e) {
            log.error("Error getting user with username: " + username, e);
            throw new ResourceNotFoundException(User.class.getSimpleName(), username);
        }
    }

    public User setNewUser(String firstName, String lastName) {
        log.info("Setting new user with first name: " + firstName + " and last name: " + lastName);
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(usernameGenerator.getDefaultUsername(firstName, lastName));
        user.setPassword(passwordGenerator.getDefaultPassword());
        user.setActive(true);
        return create(user);
    }

    public User getByUsernameForUsernameGenerator(String username) {
        Session session = factory.getCurrentSession();
        log.info("Getting user with username: " + username);
        return session.createQuery("from User where username = :username", User.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
    }
}
