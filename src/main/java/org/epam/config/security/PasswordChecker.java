package org.epam.config.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PasswordChecker {

    UserDaoImpl userDao;

    public boolean checkPassword(String username, String password) {
        log.info("Checking password for user: " + username);
        User user = userDao.getByUsername(username);
        if (user.getPassword().equals(password)){
            log.info("Password for user: " + username + " is correct");
          return true;
        }
        log.error("Wrong password while checking password for user: " + username);
        throw new VerificationException("Wrong password while checking password for user: " + username);
    }
}
