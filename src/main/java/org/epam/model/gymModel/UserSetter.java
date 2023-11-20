package org.epam.model.gymModel;

import org.epam.model.User;

/**
 * This interface is used to mark Trainer and Trainee in the project.
 * @see Trainer
 * @see Trainee
 */
public interface UserSetter {
    void setUser(User user);

    User getUser();
}
