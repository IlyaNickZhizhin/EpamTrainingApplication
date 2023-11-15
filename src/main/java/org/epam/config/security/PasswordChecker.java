package org.epam.config.security;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.epam.config.Storage;
import org.epam.model.User;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordChecker {

    Storage storage;

    public boolean checkPassword(String username, String password) {
        return ((User) storage.getUsers().get(username)).getPassword().equals(password);
    }
}
