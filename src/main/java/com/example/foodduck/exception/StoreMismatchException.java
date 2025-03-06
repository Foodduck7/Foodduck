package com.example.foodduck.exception;

public class StoreMismatchException extends RuntimeException {
    public StoreMismatchException(String message) {
        super(message);
    }
}
