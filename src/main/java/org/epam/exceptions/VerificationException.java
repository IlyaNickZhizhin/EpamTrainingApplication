package org.epam.exceptions;

import java.nio.file.AccessDeniedException;

public class VerificationException extends RuntimeException {
    public VerificationException(String message) {
        super(message);
    }
}
