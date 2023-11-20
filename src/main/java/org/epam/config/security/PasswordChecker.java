package org.epam.config.security;

import lombok.AllArgsConstructor;
import org.epam.dao.UserDaoImpl;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.springframework.stereotype.Component;

/**
 * This class is the simplest realization to check password for user.
 * @see org.epam.model.User
 */
@Component
@AllArgsConstructor
public class PasswordChecker {

    UserDaoImpl userDao;

    public boolean checkPassword(String username, String password) {
        User user = userDao.getByUsername(username);
        if (user.getPassword().equals(password)){
          return true;
        }
        throw new VerificationException("Wrong password while checking password for user: " + username);
    }
}
