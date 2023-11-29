package org.epam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.config.security.PasswordChecker;
import org.epam.dao.UserDaoImpl;
import org.epam.dto.LoginRequest;
import org.epam.exceptions.InvalidDataException;
import org.epam.exceptions.VerificationException;
import org.epam.model.User;
import org.epam.model.gymModel.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final UserDaoImpl userDao;
    private final PasswordChecker passwordChecker;

    @Transactional(readOnly = true)
    public Role login(LoginRequest request) throws VerificationException, InvalidDataException {
        User user = userDao.getByUsername(request.getUsername());
        if (user==null) throw new InvalidDataException("User with username: " + request.getUsername() + " not found");
        boolean check = passwordChecker.checkPassword(request.getUsername(), request.getPassword(), user);
        if (check) {
            log.info("User with username: " + request.getUsername() + " logged in as " + user.getRole().getClass().getSimpleName());
            return user.getRole();
        } else {
            throw new VerificationException("Wrong password");
        }
    }
}
