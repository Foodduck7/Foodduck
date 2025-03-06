package com.example.foodduck.exception;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // 주문 시간 외의 주문에 대한 예외처리: 403
    @ExceptionHandler(OutOfOrderTimeException.class)
    public ResponseEntity<String> handleOrderTimeException(OutOfOrderTimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    // 가게 최소 주문 금액 미만 주문에 대한 예외처리: 400
    @ExceptionHandler(MinimumOrderAmountException.class)
    public ResponseEntity<String> handleMinimumOrderAmountException(MinimumOrderAmountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> fieldErrorList = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)  // 각 필드의 오류 메시지를 가져온다.
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldErrorList);
    }

    // 장바구니와 메뉴의 가게가 일치하지 않을 경우에 대한 예외처리: 400
    @ExceptionHandler(StoreMismatchException.class)
    public ResponseEntity<String> handleStoreMismatchException(StoreMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ExpiredShoppingCartException.class)
    public ResponseEntity<String> handleExpiredShoppingCartException(ExpiredShoppingCartException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}