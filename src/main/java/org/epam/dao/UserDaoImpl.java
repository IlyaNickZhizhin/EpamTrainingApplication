package org.epam.dao;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.epam.config.PasswordGenerator;
import org.epam.config.UsernameGenerator;
import org.epam.model.User;
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
        try {
            save(user);
        } catch (HibernateException e) {
            throw new RuntimeException("Can't create user", e);
        }
        return user;
    }

    public void save(User user) {
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
                throw new RuntimeException("Can't save user", e);
            }
        } finally {
            session.close();
        }
    }

    public void update(int id, User user) {
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User userToUpdate = session.get(User.class, id);
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setActive(user.isActive());
            session.merge(userToUpdate);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
                throw new RuntimeException("Can't update user", e);
            }
        } finally {
            session.close();
        }
    }

    public void delete(int id) {
        Session session = factory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            User userToDelete = session.get(User.class, id);
            session.remove(userToDelete);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
                throw new RuntimeException("Can't delete user", e);
            }
        } finally {
            session.close();
        }
    }

    public User get(int id) {
        Session session = factory.openSession();
        try {
            User user = session.get(User.class, id);
            return user;
        } catch (HibernateException e) {
            throw new RuntimeException("Can't get user", e);
        } finally {
            session.close();
        }
    }

    public User get(String username) {
        Session session = factory.openSession();
        try {
            User user = session.createQuery("from User where username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return user;
        } catch (HibernateException e) {
            throw new RuntimeException("Can't get user", e);
        } finally {
            session.close();
        }
    }

    public User setNewUser(String firstName, String lastName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(UsernameGenerator.getDefaultUsername(firstName, lastName));
        user.setPassword(PasswordGenerator.getDefaultPassword());
        user.setActive(true);
        save(user);
        return user;
    }
}
