package org.epam.dao;

import org.epam.config.Storage;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserDao {

    private Map<String, User> users;

    @Autowired
    public UserDao(Storage storage) {
        this.users = storage.getUsers();
    }

    private static final AtomicInteger AUTO_ID = new AtomicInteger(0);

    Logger logger = LoggerFactory.getLogger(Storage.class);

    public int create(User user) {
        logger.info("Creating user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        user.setId(AUTO_ID.incrementAndGet());
        try {
            save(user);
        } catch (InvalidDataException e) {
            logger.error("Invalid data exception: " + e.getMessage());
            throw new InvalidDataException("create(User user)");
        }
        logger.info("User with first name: " + user.getFirstName() + " and last name: " + user.getLastName() + " created");
        return user.getId();
    }


    public void save(User user) {
        logger.info("Saving user with username: " + user.getUsername());
        try {
        users.put(user.getUsername(), user);
        } catch (InvalidDataException e) {
            logger.error("Invalid data exception: " + e.getMessage());
            throw new InvalidDataException("save(User user)");
        }
        logger.info("User with username: " + user.getUsername() + " saved");
    }

    public void update(int id, User user) {
        logger.info("Updating user with username: " + user.getUsername());
        User userToUpdate = users.get(user.getUsername());
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setActive(user.isActive());
        try {
            save(userToUpdate);
        } catch (InvalidDataException e) {
            logger.error("Invalid data exception: " + e.getMessage());
            throw new InvalidDataException("update(int id, User user)");
        }
        logger.info("User with username: " + user.getUsername() + " updated");
    }

    public void delete(String username) {
        logger.info("Deleting user with username: " + username);
        try {
            users.remove(username);
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found exception: " + e.getMessage());
            throw new ResourceNotFoundException("User", username);
        } finally {
            logger.info("Deleting User with username: " + username + " ended");
        }
    }

    public User get(String username) {
        logger.info("Getting user with username: " + username);
        try {
            return users.get(username);
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found exception: " + e.getMessage());
            throw new ResourceNotFoundException("User", username);
        } finally {
            logger.info("Geting user with username: " + username + " was ended");
        }
    }

    private String defaultUsername(String firstName, String lastName) {
        logger.info("Creating default username for user with first name: " + firstName + " and last name: " + lastName);
        StringBuffer username = new StringBuffer(firstName.concat("." + lastName));
        int indexOfUsername = 1;
        if (users.get(username.toString())!=null) {
            logger.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " already exists");
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        while (users.get(username.toString())!=null) {
            username.delete((username.length()-String.valueOf(indexOfUsername).length()),username.length());
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        logger.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " created as " + username.toString() );
        return username.toString();
    }

    public String defaultPassword() {
        logger.info("Creating default password for user");
        StringBuffer pass = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            pass.append((char) (Math.random() * 26 + 97));
        }
        logger.info("Default password for user created as " + pass.toString());
        return pass.toString();
    }

    public User setNewUser(String firstName, String lastName) {
        logger.info("Creating new user with first name: " + firstName + " and last name: " + lastName);
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        String username = defaultUsername(firstName, lastName);
        user.setUsername(username);
        user.setPassword(defaultPassword());
        user.setActive(true);
        logger.info("New user with first name: " + firstName + " and last name: " + lastName + " created");
        return user;
    }
}
