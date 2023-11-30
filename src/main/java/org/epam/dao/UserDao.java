package org.epam.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.exceptions.InvalidDataException;
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


    public User create(User user) {
        log.info("Creating new user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        save(user);
        return user;
    }

    public void save(User user) {
        log.info("Saving user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        factory.getCurrentSession().persist(user);
    }

    public User update(int id, User user) {
        log.info("Updating user with id: " + id);
        Session session = factory.getCurrentSession();
        Optional<User> optionalUser = Optional.ofNullable(session.merge(user));
        return optionalUser.orElseThrow(() -> new InvalidDataException("update(" + id + ", " + user.getUsername() + " and other fields)",
                "No user with id: " + id));
    }

    public User delete(int id) {
        log.info("Deleting user with id: " + id);
        Session session = factory.getCurrentSession();
        User user = session.get(User.class, id);
        session.remove(user);
        return user;
    }

    public User get(int id) {
        log.info("Getting user with id: " + id);
        Session session = factory.getCurrentSession();
        Optional<User> userOptional = Optional.ofNullable(session.get(User.class, id));
        return userOptional.orElseThrow(() -> new InvalidDataException("get(" + id + ")", "No user with id: " + id));
    }

    public User getByUsername(String username) throws VerificationException{
        Session session = factory.getCurrentSession();
        log.info("Getting user with username: " + username);
        Optional<User> optionalUser = Optional.ofNullable(session.createQuery("from User where username = :username", User.class)
                .setParameter("username", username)
                .getSingleResultOrNull());
        return optionalUser.orElseThrow(() -> new InvalidDataException("getByUsername(" + username + ")", "No user with username: " + username));
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

    public List<User> getAll(){
        log.info("Getting all users");
        Optional<List<User>> optionalUsers = Optional.ofNullable(factory.getCurrentSession()
                .createQuery("from User", User.class).list());
        return optionalUsers.orElseThrow(() -> new InvalidDataException("getAll()", "No users in database"));
    }
}
