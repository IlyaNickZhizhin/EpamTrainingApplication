package org.epam.exceptions;

/**
 * This exception is thrown when user try to do something that he can't do.
 * or as mock for methods are not prohibited by the task at this moment.
 * @see org.epam.service.GymAbstractService
 * @see org.epam.service.TraineeService
 * @see org.epam.service.TrainerService
 * @see org.epam.service.TrainingService
 */
public class ProhibitedAction extends RuntimeException{
    public ProhibitedAction(String message) {
        super(message);
    }
}
