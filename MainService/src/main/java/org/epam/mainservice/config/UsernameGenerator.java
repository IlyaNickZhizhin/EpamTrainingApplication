package org.epam.mainservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.mainservice.repository.UserRepository;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class UsernameGenerator {

    private final UserRepository userDao;

    public String getDefaultUsername(String firstName, String lastName) {
        log.info("Creating default username for user with first name: " + firstName.substring(0,0)
                + ". and last name: " + lastName.substring(0,0)+ ".");
        StringBuilder username = new StringBuilder(firstName.concat("." + lastName));
        int indexOfUsername = 1;
        if (userDao.findByUsername(username.toString()).isPresent()) {
            log.info("Default username for user with first name: " + firstName.substring(0,0) + ". and last name: "
                    + lastName.substring(0,0) + ". already exists");
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        while (userDao.findByUsername(username.toString()).isPresent()) {
            username.delete((username.length()-String.valueOf(indexOfUsername).length()),username.length());
            username.append(indexOfUsername);
            indexOfUsername++;
        }
        log.info("Default username for user with first name: " + firstName.substring(0,0)
                + ". and last name: " + lastName.substring(0,0) + ". created with username: "
                + firstName.substring(0,0) + "." + lastName.substring(0,0) + (indexOfUsername-1));
        return username.toString();
    }

}
