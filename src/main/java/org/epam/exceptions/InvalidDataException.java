package org.epam.exceptions;

/**
 * This exception is thrown when parameters for methods are invalid.
 * @see org.epam.service.GymAbstractService
 * @see org.epam.service.TraineeService
 * @see org.epam.service.TrainerService
 * @see org.epam.service.TrainingService
 */
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String methodName) {
        super("Invalid data in method " + methodName);
    }
}
