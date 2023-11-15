package org.epam.config;

import lombok.RequiredArgsConstructor;
import org.epam.model.User;
import org.epam.storageInFile.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@DependsOn("dataInitializer")
public class UsernameGenerator {

    static Storage<User> storage;

    @Autowired
    public UsernameGenerator(Storage<User> storage) {
        this.storage = storage;
    }

    public static String getDefaultUsername(String firstName, String lastName) {

        Logger logger = Logger.getLogger(UsernameGenerator.class.getName());
        logger.info("Creating default username for user with first name: " + firstName + " and last name: " + lastName);
        StringBuffer username = new StringBuffer(firstName.concat("." + lastName));
        int indexOfUsername = 1;
        if (storage.getUsers().containsKey(username.toString())) {
            logger.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " already exists");
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        while (storage.getUsers().containsKey(username.toString())) {
            username.delete((username.length()-String.valueOf(indexOfUsername).length()),username.length());
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        logger.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " created as " + username.toString() );
        return username.toString();
    }

}
