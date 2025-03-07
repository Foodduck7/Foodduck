package com.example.foodduck.exception.handler;

import com.example.foodduck.exception.custom.ApplicationException;
import com.example.foodduck.exception.custom.MinimumOrderAmountException;
import com.example.foodduck.exception.custom.OutOfOrderTimeException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /*
    03.06.18:28, 관리자 대시보드 테스트 시, 500eroor 따라서, 뭐 때문인지 찍어보려고 수정.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        logger.error("서버 오류 발생: ", ex); //로그에 예외 메시지 출력
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생: " + ex.getMessage());
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

    // 사용자 정의 예외 처리
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<String> handleApplication(ApplicationException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

}