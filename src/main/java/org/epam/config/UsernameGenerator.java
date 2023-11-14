package org.epam.config;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.logging.Logger;

@Component
public class UsernameGenerator {

    public static String getDefaultUsername(String firstName, String lastName, HashSet<String> users) {

        Logger logger = Logger.getLogger(UsernameGenerator.class.getName());
        logger.info("Creating default username for user with first name: " + firstName + " and last name: " + lastName);
        StringBuffer username = new StringBuffer(firstName.concat("." + lastName));
        int indexOfUsername = 1;
        if (users.contains(username.toString())) {
            logger.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " already exists");
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        while (users.contains(username.toString())) {
            username.delete((username.length()-String.valueOf(indexOfUsername).length()),username.length());
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        logger.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " created as " + username.toString() );
        return username.toString();
    }

}
