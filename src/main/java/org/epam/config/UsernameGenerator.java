package org.epam.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.dao.UserDaoImpl;
import org.epam.model.User;

import java.util.logging.Logger;

@AllArgsConstructor
@Slf4j
public class UsernameGenerator {

    private UserDaoImpl userDao;

    public String getDefaultUsername(String firstName, String lastName) {
        log.info("Creating default username for user with first name: " + firstName + " and last name: " + lastName);
        StringBuilder username = new StringBuilder(firstName.concat("." + lastName));
        int indexOfUsername = 1;
        if (userDao.getByUsernameForUsernameGenerator(username.toString())!=null) {
            log.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " already exists");
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        while (userDao.getByUsernameForUsernameGenerator(username.toString())!=null) {
            username.delete((username.length()-String.valueOf(indexOfUsername).length()),username.length());
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        log.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " created as " + username. toString() );
        return username.toString();
    }

}
