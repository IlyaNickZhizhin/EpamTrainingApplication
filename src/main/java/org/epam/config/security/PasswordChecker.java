package org.epam.config.security;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.epam.dao.UserDaoImpl;
import org.epam.storageInFile.Storage;
import org.epam.model.User;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordChecker {

    UserDaoImpl userDao;

    public boolean checkPassword(String username, String password) {
        User user = userDao.get(username);
        return user.getPassword().equals(password);
    }
}
