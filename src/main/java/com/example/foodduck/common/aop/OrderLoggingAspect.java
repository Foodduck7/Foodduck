package com.example.foodduck.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 주문 관련 변경 사항 로그 수행
 * @author 이호수
 * @version 주문 생성, 삭제와 주문 상태 업데이트 로깅 메서드 구현
 * 테스트 수행 필요
*/
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderLoggingAspect {

    private final HttpServletRequest httpServletRequest;
    private final ObjectMapper objectMapper;

    // 주문 요청과 삭제 로그
    @Around("execution(* com.example.foodduck.order..*createOrder(..)) " +
            "|| execution(* com.example.foodduck.order..*deleteOrder(..))")
    public Object logOrderRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        Long storeId = (Long) httpServletRequest.getAttribute("storeId");
        Long orderId = (Long) httpServletRequest.getAttribute("orderId");
        String url = httpServletRequest.getRequestURI();
        long requestTimestamp = System.currentTimeMillis();
        String requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());
        log.info("AOP - Order API Request: StoreId={}, OrderId={} Timestamp={}, URL={}, RequestBody={}"
                , storeId, orderId, requestTimestamp, url, requestBody);
        Object result = joinPoint.proceed();
        String responseBody = objectMapper.writeValueAsString(result);
        long responseTimestamp = System.currentTimeMillis();
        log.info("AOP - Order API Response: StoreId={}, OrderId={}, Timestamp={}, URL={}, ResponseBody={}"
                , storeId, orderId, responseTimestamp, url, responseBody);
        return result;
    }

    // 주문 상태 변경 로그
    @Around("execution(* com.example.foodduck.order..*updateOrderState(..))")
    public Object logOrderState(ProceedingJoinPoint joinPoint) throws Throwable {
        Long storeId = (Long) httpServletRequest.getAttribute("storeId");
        Long orderId = (Long) httpServletRequest.getAttribute("orderId");
        String url = httpServletRequest.getRequestURI();
        long requestTimestamp = System.currentTimeMillis();
        String requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());
        log.info("AOP - Order API State Update Request: StoreId={}, OrderId={}, Timestamp={}, URL={}, RequestBody={}",
                storeId, orderId, requestTimestamp, url, requestBody);
        Object result = joinPoint.proceed();
        String responseBody = objectMapper.writeValueAsString(result);
        log.info("AOP - Order API State Update Request: StoreId={}, OrderId={}, Timestamp={}, URL={}, ResponseBody={}",
                storeId, orderId, url, requestTimestamp, responseBody);
        return result;
    }
}
