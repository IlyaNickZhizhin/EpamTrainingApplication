package org.epam.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {

    private static Logger logger = LoggerFactory.getLogger(PasswordGenerator.class.getName());

    public static String getDefaultPassword() {
        logger.info("Creating default password for user");
        StringBuffer pass = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            pass.append((char) (Math.random() * 26 + 97));
        }
        logger.info("Default password for user created as " + pass.toString());
        return pass.toString();
    }

}
