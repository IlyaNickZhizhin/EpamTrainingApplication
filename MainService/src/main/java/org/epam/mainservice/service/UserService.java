package org.epam.mainservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.epam.mainservice.config.PasswordGenerator;
import org.epam.mainservice.config.UsernameGenerator;
import org.epam.mainservice.model.Role;
import org.epam.mainservice.model.User;
import org.epam.mainservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGenerator passwordGenerator;
    private final PasswordEncoder passwordEncoder;

    public ImmutablePair<Optional<User>, String> setNewUser(String firstName, String lastName, Role role) {
        log.info("Setting new user with first name: " + firstName.substring(0,0) + ". and last name: "
                + lastName.substring(0,0) + "***");
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(usernameGenerator.getDefaultUsername(firstName, lastName));
        String password = passwordGenerator.getDefaultPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setActive(true);
        user.setRoles(Set.of(role));
        userRepository.save(user);
        return ImmutablePair.of(Optional.of(user), password);
    }


    public Optional<User> update(int id, User user) {
        log.info("Updating user with id: " + id);
        return Optional.of(userRepository.save(user));
    }

    public Optional<User> delete(int id) {
        log.info("Deleting user with id: " + id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            log.info("Deleting user with id: " + id);
            userRepository.delete(user.get());
        } else {
            log.error("Error deleting user with id: " + id);
        }
        return user;
    }

    public Optional<User> findByUsername(String username) {
        log.info("Getting user with username: " + username.substring(0,0) +"***");
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
