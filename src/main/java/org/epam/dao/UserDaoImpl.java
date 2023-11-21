package org.epam.dao;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.exceptions.ResourceNotFoundException;
import org.epam.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * This class is the DAO for User models.
 * @see org.epam.model.User
 * @see org.epam.model.gymModel.UserSetter
 * @see org.epam.model.gymModel.Model
 * @see org.epam.model.gymModel.Trainee
 * @see org.epam.model.gymModel.Trainer
 * @see org.epam.dao.GymAbstractDaoImpl
 * @see org.epam.dao.UserDaoImpl#create(User)
 * @see org.epam.dao.UserDaoImpl#save(User)
 * @see org.epam.dao.UserDaoImpl#update(int, User)
 * @see org.epam.dao.UserDaoImpl#delete(int)
 * @see org.epam.dao.UserDaoImpl#get(int)
 * @see org.epam.dao.UserDaoImpl#getByUsername(String)
 * @see org.epam.dao.UserDaoImpl#setNewUser(String, String)
 */
@Repository
@AllArgsConstructor
@Slf4j
@Transactional
public class UserDaoImpl {

    SessionFactory factory;
    PasswordGenerator passwordGenerator;
    UsernameGenerator usernameGenerator;

    /**
     * This method creates a new User in the database. It logs an informational message before saving the User.
     * @param user
     * @return The User that was created.
     */
    public User create(User user) {
        log.info("Creating user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        try {
            factory.getCurrentSession().persist(user);
        } catch (Exception e) {
            log.error("Error creating user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
            throw new ResourceNotFoundException(User.class.getSimpleName(), user.getUsername());
        }
        return user;
    }

    /**
     * This method saves a User in the database. It logs an informational message before saving the User.
     * @param user
     */
    public void save(User user) {
        log.info("Creating user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
        try {
            factory.getCurrentSession().persist(user);
        } catch (Exception e) {
            log.error("Error creating user with first name: " + user.getFirstName() + " and last name: " + user.getLastName());
            throw new ResourceNotFoundException(User.class.getSimpleName(), user.getUsername());
        }
    }

    /**
     * This method updates a User in the database using its ID and an updated User object. It logs an informational message before the update operation.
     * @param id The ID of the User to be updated.
     * @param user The User object containing the updated data.
     */
    public void update(int id, User user) {
        log.info("Updating user with id: " + id);
        Session session = factory.getCurrentSession();
        User userToUpdate = session.get(User.class, id);
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setActive(user.isActive());
        try {
            session.merge(userToUpdate);
        } catch (Exception e) {
            log.error("Error updating user with id: " + id, e);
            throw new ResourceNotFoundException(User.class.getSimpleName(), id);
        }
    }

    /**
     * This method deletes a User from the database using its ID.
     * It logs an informational message before the delete operation.
     * @param id
     */
    public void delete(int id) {
        log.info("Deleting user with id: " + id);
        Session session = factory.getCurrentSession();
        User user = session.get(User.class, id);
        try {
            session.remove(user);
        } catch (Exception e) {
            log.error("Error deleting user with id: " + id, e);
            throw new ResourceNotFoundException(User.class.getSimpleName(), id);
        }
    }

    /**
     * This method retrieves a User from the database using its ID.
     * @param id
     * @return The User with the specified ID.
     */
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

    /**
     * This method retrieves a User from the database using its username.
     * @param username
     * @return The User with the specified username.
     */
    public User getByUsername(String username) {
        Session session = factory.getCurrentSession();
        try {
            log.info("Getting user with username: " + username);
            return session.createQuery("from User where username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResultOrNull();
        } catch (Exception e) {
            log.error("Error getting user with username: " + username, e);
            throw new ResourceNotFoundException(User.class.getSimpleName(), username);
        }
    }

    /**
     * This method creates and sets a new User in the database using the User's first name and last name.
     * @param firstName
     * @param lastName
     * @return The User that was created by first and last names with default username and password.
     */
    public User setNewUser(String firstName, String lastName) {
        log.info("Setting new user with first name: " + firstName + " and last name: " + lastName);
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(usernameGenerator.getDefaultUsername(firstName, lastName));
        user.setPassword(passwordGenerator.getDefaultPassword());
        user.setActive(true);
        try {
            return create(user);
        } catch (ResourceNotFoundException e) {
            log.error("Error setting new user with first name: " + firstName + " and last name: " + lastName, e);
            throw e;
        }
    }
}
