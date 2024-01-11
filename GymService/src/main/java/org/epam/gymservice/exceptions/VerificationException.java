package org.epam.gymservice.exceptions;

/**
 * This exception is thrown when verification of password or username failed.
 */
public class VerificationException extends RuntimeException {
    public VerificationException(String message) {
        super(message);
    }
}
