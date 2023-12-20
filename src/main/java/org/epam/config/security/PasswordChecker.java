package org.epam.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PasswordChecker {

    public boolean checkPassword(String username, String password, User user) {
        log.info("Checking password for username: " + username.substring(0,0) +"***");
        if (user.getPassword().equals(password)){
            log.info("Password for username: " + username.substring(0,0) + " is correct");
          return true;
        }
        log.error("Wrong password while checking password for username: " + username.substring(0,0));
        throw new VerificationException("Wrong password while checking password for username: "
                + username.substring(0,0));
    }
}
