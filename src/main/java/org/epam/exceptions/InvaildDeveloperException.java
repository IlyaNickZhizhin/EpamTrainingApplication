package org.epam.exceptions;

/**
 * This exception is thrown when program work not correct
 * and I have no idea why it happened.
 * @see org.epam.service.GymAbstractService
 * @see org.epam.service.TraineeService
 * @see org.epam.service.TrainerService
 * @see org.epam.service.TrainingService
 */
public class InvaildDeveloperException extends RuntimeException{
    public InvaildDeveloperException(String methodName) {
        super("Invalid developer wrote invalid method " + methodName);
    }
}
