package org.epam.exceptions;


public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("Resource of type " + resourceType + " with id " + resourceId + " not found");
    }
}


