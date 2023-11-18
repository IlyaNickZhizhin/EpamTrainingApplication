package org.epam.exceptions;

public class ProhibitedAction extends RuntimeException{
    public ProhibitedAction(String message) {
        super(message);
    }
}
