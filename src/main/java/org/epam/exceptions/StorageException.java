package org.epam.exceptions;

/**
 * This exception is thrown when resource not found in file storage
 */
public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super("There is no such " + message + " in storage");
    }
}



