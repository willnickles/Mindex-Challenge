package com.mindex.challenge.exception;

/**
 * Custom exception to handle cases where compensation for a specific employee is not found.
 */
public class CompensationNotFoundException extends RuntimeException {
    public CompensationNotFoundException(String message) {
        super(message);
    }
}
