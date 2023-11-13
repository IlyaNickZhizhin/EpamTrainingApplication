package org.epam.exceptions;

public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super("There is no such " + message + " in storage");
    }
}



