package com.example.foodduck.exception.custom;

import org.springframework.http.HttpStatus;

/**
 * 주문 시간 외의 주문에 대한 예외
 * @author 이호수
 * @version 런타임 시점에 발생 가능한 예외
 */
public class MinimumOrderAmountException extends ApplicationException {
    public MinimumOrderAmountException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
