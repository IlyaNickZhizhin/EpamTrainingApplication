package org.epam.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDao {

    private final EntityManager entityManager;
    private final PasswordGenerator passwordGenerator;
    private final UsernameGenerator usernameGenerator;


    public Optional<User> create(User user) {
        log.info("Creating new user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        save(user);
        return Optional.ofNullable(user);
    }

    public void save(User user) {
        log.info("Saving user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        entityManager.persist(user);
    }

    public Optional<User> update(int id, User user) {
        log.info("Updating user with id: " + id);
        return Optional.ofNullable(entityManager.merge(user));
    }

    public Optional<User> delete(int id) {
        log.info("Deleting user with id: " + id);
        User user = entityManager.find(User.class, id);
        entityManager.remove(user);
        return Optional.ofNullable(user);
    }

    public Optional<User> get(int id) {
        log.info("Getting user with id: " + id);
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    public Optional<User> getByUsername(String username) throws VerificationException{
        log.info("Getting user with username: " + username);
        try {
            return Optional.ofNullable(entityManager.createQuery("from User where username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult());
        } catch (NoResultException e) {
            log.error("User with username: " + username + " not found");
            return Optional.empty();
        }
    }

    public Optional<User> setNewUser(String firstName, String lastName) {
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
        try {
            return entityManager.createQuery("from User where username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            log.error("User with username: " + username + " not found");
            return null;
        }
    }

    public Optional<List<User>> getAll(){
        log.info("Getting all users");
        return Optional.ofNullable(entityManager
                .createQuery("from User", User.class).getResultList());
    }
}
