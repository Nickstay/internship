package org.mts.internship.exception;

public class DestinationException extends RuntimeException {
    public DestinationException(long id) {
        super("Worker already in this department by id: " + id);
    }
}
