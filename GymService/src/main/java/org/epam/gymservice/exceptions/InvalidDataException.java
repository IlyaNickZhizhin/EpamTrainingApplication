package org.epam.gymservice.exceptions;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String methodName, String message) {
        super("Invalid data in method " + methodName + ": " + message);
    }
}
