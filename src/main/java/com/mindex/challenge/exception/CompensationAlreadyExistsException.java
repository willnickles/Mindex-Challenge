package com.mindex.challenge.exception;

/**
 * Custom exception to handle cases where compensation for a specific employee already exists to use a put endpoint instead.
 */
public class CompensationAlreadyExistsException extends RuntimeException {
    public CompensationAlreadyExistsException(String message) {
        super(message);
    }
}
