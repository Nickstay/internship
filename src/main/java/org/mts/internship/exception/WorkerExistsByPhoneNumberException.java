package org.mts.internship.exception;

public class WorkerExistsByPhoneNumberException extends RuntimeException {
    public WorkerExistsByPhoneNumberException(String phoneNumber) {
        super("Worker already registered by phone number: " + phoneNumber);
    }
}
