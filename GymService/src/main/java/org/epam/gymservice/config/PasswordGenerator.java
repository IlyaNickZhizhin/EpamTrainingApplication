package org.epam.gymservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordGenerator {

    public String getDefaultPassword() {
        StringBuilder pass = new StringBuilder();
        log.info("Creating default password for user");
        for (int i = 0; i < 10; i++) {
            pass.append((char) (Math.random() * 26 + 97));
        }
        log.info("Default password for user created as *********");
        return pass.toString();
    }

}
