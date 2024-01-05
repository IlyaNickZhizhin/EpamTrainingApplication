package org.epam.gymservice.exceptions;

public class ProhibitedActionException extends RuntimeException{
    public ProhibitedActionException(String message) {
        super(message);
    }
}
