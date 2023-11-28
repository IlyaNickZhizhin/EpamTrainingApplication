package org.epam.exceptions;

import org.epam.service.GymGeneralService;

/**
 * This exception is thrown when user try to do something that he can't do.
 * or as mock for methods are not prohibited by the task at this moment.
 * @see GymGeneralService
 * @see org.epam.service.TraineeService
 * @see org.epam.service.TrainerService
 * @see org.epam.service.TrainingService
 */
public class ProhibitedActionException extends RuntimeException{
    public ProhibitedActionException(String message) {
        super(message);
    }
}
