package org.epam.exceptions;

import org.epam.service.GymGeneralService;

/**
 * This exception is thrown when parameters for methods are invalid.
 * @see GymGeneralService
 * @see org.epam.service.TraineeService
 * @see org.epam.service.TrainerService
 * @see org.epam.service.TrainingService
 */
public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String methodName) {
        super("Invalid data in method " + methodName);
    }
}
