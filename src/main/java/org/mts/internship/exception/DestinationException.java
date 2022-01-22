package org.mts.internship.exception;

public class DestinationException extends RuntimeException {
    public DestinationException(String name) {
        super("Worker is already in department: " + name);
    }
}
