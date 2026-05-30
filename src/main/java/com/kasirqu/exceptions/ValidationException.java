package com.kasirqu.exceptions;

/**
 * Thrown when input validation fails in CREATE services.
 * Used for empty names, invalid prices, invalid quantities, etc.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
