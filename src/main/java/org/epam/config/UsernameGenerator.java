package org.epam.config;

import org.epam.dao.UserDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@DependsOn("dataInitializer")
public class UsernameGenerator {

    private static UserDaoImpl userDao;

    @Autowired
    public UsernameGenerator(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    public static String getDefaultUsername(String firstName, String lastName) {

        Logger logger = Logger.getLogger(UsernameGenerator.class.getName());
        logger.info("Creating default username for user with first name: " + firstName + " and last name: " + lastName);
        StringBuilder username = new StringBuilder(firstName.concat("." + lastName));
        int indexOfUsername = 1;
        if (userDao.getByUsername(username.toString())!=null) {
            logger.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " already exists");
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        while (userDao.getByUsername(username.toString())!=null) {
            username.delete((username.length()-String.valueOf(indexOfUsername).length()),username.length());
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        logger.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " created as " + username. toString() );
        return username.toString();
    }

}
