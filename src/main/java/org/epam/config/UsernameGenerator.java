package org.epam.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.dao.UserRepository;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class UsernameGenerator {

    private final UserRepository userDao;

    public String getDefaultUsername(String firstName, String lastName) {
        log.info("Creating default username for user with first name: " + firstName + " and last name: " + lastName);
        StringBuilder username = new StringBuilder(firstName.concat("." + lastName));
        int indexOfUsername = 1;
        if (userDao.findByUsername(username.toString()).isPresent()) {
            log.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " already exists");
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        while (userDao.findByUsername(username.toString()).isPresent()) {
            username.delete((username.length()-String.valueOf(indexOfUsername).length()),username.length());
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        log.info("Default username for user with first name: " + firstName + " and last name: " + lastName + " created with username: " + username. toString() );
        return username.toString();
    }

}
