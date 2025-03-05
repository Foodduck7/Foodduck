package com.example.foodduck.exception;

/**
 * 가게 운영 시간 외에 주문에 대한 예외
 * @author 이호수
 * @version 런타임 시점에 발생 가능한 예외
 */
public class OutOfOrderTimeException extends RuntimeException {
    public OutOfOrderTimeException(String message) {
        super(message);
    }
}
