package org.epam.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDao {

    private final SessionFactory factory;
    private final PasswordGenerator passwordGenerator;
    UsernameGenerator usernameGenerator = new UsernameGenerator(this);


    public Optional<User> create(User user) {
        log.info("Creating new user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        save(user);
        return Optional.ofNullable(user);
    }

    public void save(User user) {
        log.info("Saving user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        factory.getCurrentSession().persist(user);
    }

    public Optional<User> update(int id, User user) {
        log.info("Updating user with id: " + id);
        Session session = factory.getCurrentSession();
        return Optional.ofNullable(session.merge(user));
    }

    public Optional<User> delete(int id) {
        log.info("Deleting user with id: " + id);
        Session session = factory.getCurrentSession();
        User user = session.get(User.class, id);
        session.remove(user);
        return Optional.ofNullable(user);
    }

    public Optional<User> get(int id) {
        log.info("Getting user with id: " + id);
        Session session = factory.getCurrentSession();
        return Optional.ofNullable(session.get(User.class, id));
    }

    public Optional<User> getByUsername(String username) throws VerificationException{
        Session session = factory.getCurrentSession();
        log.info("Getting user with username: " + username);
        return Optional.ofNullable(session.createQuery("from User where username = :username", User.class)
                .setParameter("username", username)
                .getSingleResultOrNull());
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
        Session session = factory.getCurrentSession();
        log.info("Getting user with username: " + username);
        return session.createQuery("from User where username = :username", User.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
    }

    public Optional<List<User>> getAll(){
        log.info("Getting all users");
        return Optional.ofNullable(factory.getCurrentSession()
                .createQuery("from User", User.class).list());
    }
}
