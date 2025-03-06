package com.example.foodduck.exception.custom;

import org.springframework.http.HttpStatus;

public class ExpiredShoppingCartException extends ApplicationException {
    public ExpiredShoppingCartException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
