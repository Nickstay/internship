package org.mts.internship.exception;

public class WorkerNotFoundException extends RuntimeException {
    public WorkerNotFoundException(long id) {super("Worker not found by id: " + id);}
}
