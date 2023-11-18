package org.epam.dao.storage;

import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.User;
import org.epam.storageInFile.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

import static org.epam.config.PasswordGenerator.getDefaultPassword;
import static org.epam.config.UsernameGenerator.getDefaultUsername;

@DependsOn("dataInitializer")
@Repository
public class UserDaoStorageImpl {

    private final Storage<User> storage;

    @Autowired
    public UserDaoStorageImpl(Storage<User> storage) {
        this.storage = storage;
    }

    private static final AtomicInteger AUTO_ID = new AtomicInteger(0);

    Logger logger = LoggerFactory.getLogger(Storage.class);

    public User create(User user) {
        logger.info("Creating user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        user.setId(AUTO_ID.incrementAndGet());
        try {
            save(user);
        } catch (InvalidDataException e) {
            logger.error("Invalid data exception: " + e.getMessage());
            throw new InvalidDataException("create(User user)");
        }
        logger.info("User with first name: " + user.getFirstName() + " and last name: " + user.getLastName() + " created");
        return user;
    }


    public void save(User user) {
        logger.info("Saving user with username: " + user.getUsername());
        try {
            storage.getUsers().put(user.getUsername(), user);
        } catch (InvalidDataException e) {
            logger.error("Invalid data exception: " + e.getMessage());
            throw new InvalidDataException("save(User user)");
        }
        logger.info("User with username: " + user.getUsername() + " saved");
    }

    public void update(User user) {
        logger.info("Updating user with username: " + user.getUsername());
        User userToUpdate = storage.getUsers().get(user.getUsername());
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
            storage.getUsers().remove(username);
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
            return storage.getUsers().get(username);
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found exception: " + e.getMessage());
            throw new ResourceNotFoundException("User", username);
        } finally {
            logger.info("Geting user with username: " + username + " was ended");
        }
    }

    public User setNewUser(String firstName, String lastName) {
        logger.info("Creating new user with first name: " + firstName + " and last name: " + lastName);
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        String username = getDefaultUsername(firstName, lastName);
        user.setUsername(username);
        user.setPassword(getDefaultPassword());
        user.setActive(true);
        logger.info("New user with first name: " + firstName + " and last name: " + lastName + " created");
        return user;
    }

    public User getById(int userId) {
        logger.info("Getting user with id: " + userId);
        for (User user :
                storage.getUsers().values()) {
            if (user.getId() == userId) {
                logger.info("User with id: " + userId + " found");
                return user;
            }
        }
        logger.error("Resource not found exception: " + userId);
        throw new ResourceNotFoundException("User", "User id#" + userId);
    }
}
