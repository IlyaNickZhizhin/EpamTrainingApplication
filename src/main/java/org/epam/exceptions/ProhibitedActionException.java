package org.epam.exceptions;

public class ProhibitedActionException extends RuntimeException{
    public ProhibitedActionException(String message) {
        super(message);
    }
}
