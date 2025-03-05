package com.example.foodduck.exception;

/**
 * 주문 시간 외의 주문에 대한 예외
 * @author 이호수
 * @version 런타임 시점에 발생 가능한 예외
 */
public class MinimumOrderAmountException extends RuntimeException {
    public MinimumOrderAmountException(String message) {
        super(message);
    }
}
