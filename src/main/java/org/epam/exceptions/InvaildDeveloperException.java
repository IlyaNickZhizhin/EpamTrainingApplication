package org.epam.exceptions;

public class InvaildDeveloperException extends RuntimeException{
    public InvaildDeveloperException(String methodName) {
        super("Invalid developer wrote invalid method " + methodName);
    }
}
