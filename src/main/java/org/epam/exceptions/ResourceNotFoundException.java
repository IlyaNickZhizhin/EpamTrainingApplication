package org.epam.exceptions;

/**
 * This exception is thrown when resource not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, int resourceId) {
        super("Resource of type " + resourceType + " with id " + resourceId + " not found");
    }

    public ResourceNotFoundException(String resourceType, String resourceUsername) {
        super("Resource of type " + resourceType + " with username " + resourceUsername + " not found");
    }
}


