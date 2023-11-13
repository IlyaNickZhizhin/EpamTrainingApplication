package org.epam.exceptions;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String methodName) {
        super("Invalid data in method " + methodName);
    }
}
