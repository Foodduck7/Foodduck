package com.example.foodduck.exception.custom;

import org.springframework.http.HttpStatus;

public class StoreMismatchException extends ApplicationException {
    public StoreMismatchException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
