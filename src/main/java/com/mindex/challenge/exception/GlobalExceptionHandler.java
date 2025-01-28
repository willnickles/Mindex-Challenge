package com.mindex.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Handle CompensationAlreadyExistsException
    @ExceptionHandler(CompensationAlreadyExistsException.class)
    public ResponseEntity<String> handleCompensationAlreadyExists(CompensationAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT); // 409 Conflict
    }

    // Handle CompensationNotFoundException
    @ExceptionHandler(CompensationNotFoundException.class)
    public ResponseEntity<String> handleCompensationNotFound(CompensationNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle IllegalArgumentException (e.g., missing or invalid input)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    

    // Handle all other exceptions (fallback handler)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
