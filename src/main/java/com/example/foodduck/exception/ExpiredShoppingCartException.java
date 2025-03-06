package com.example.foodduck.exception;

public class ExpiredShoppingCartException extends RuntimeException {
    public ExpiredShoppingCartException(String message) {
        super(message);
    }
}
