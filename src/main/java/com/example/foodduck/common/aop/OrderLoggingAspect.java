package com.example.foodduck.common.aop;

import com.example.foodduck.order.dto.request.OrderCreateRequest;
import com.example.foodduck.order.dto.request.OrderUpdateRequest;
import com.example.foodduck.order.dto.response.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
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

    // 주문 요청 로그
    @Around("execution(* com.example.foodduck.order..*createOrder(..))")
    public Object logOrderCreate(ProceedingJoinPoint joinPoint) throws Throwable {
        OrderCreateRequest request = (OrderCreateRequest) joinPoint.getArgs()[0];
        Long userId = request.getUserId();
        Long menuId = request.getMenuId();
        String url = httpServletRequest.getRequestURI();
        long requestTimestamp = System.currentTimeMillis();
        String requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());
        log.info("AOP - Order API Request: User Id={}, Menu Id={} Timestamp={}, URL={}, RequestBody={}"
                , userId, menuId, requestTimestamp, url, requestBody);
        Object result = joinPoint.proceed();
        if (result instanceof OrderResponse orderResponse) {
                Long orderId = orderResponse.id();
                Long storeId = orderResponse.storeId();
                log.info("AOP - Order API Response: StoreId={}, OrderId={}, Timestamp={}, URL={}, ResponseBody={}",
                        storeId, orderId, System.currentTimeMillis(), url, objectMapper.writeValueAsString(result));
        }
        return result;
    }

    // 주문 상태 변경 로그
    @Around("execution(* com.example.foodduck.order..*updateOrderState(..))")
    public Object logOrderState(ProceedingJoinPoint joinPoint) throws Throwable {
        OrderUpdateRequest request = (OrderUpdateRequest) joinPoint.getArgs()[0];
        Long orderId = request.getOrderId();
        String ownerApprovalStatus = request.getOwnerApprovalStatus();
        String url = httpServletRequest.getRequestURI();
        long requestTimestamp = System.currentTimeMillis();
        String requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());
        log.info("AOP - Order API State Update Request: Order Id={}, Owner Approval={}, Timestamp={}, URL={}, RequestBody={}",
                orderId, ownerApprovalStatus, requestTimestamp, url, requestBody);
        Object result = joinPoint.proceed();
        if (result instanceof OrderResponse orderResponse) {
            orderId = orderResponse.id();
            Long storeId = orderResponse.storeId();
            log.info("AOP - Order API State Update Response: StoreId={}, OrderId={}, Timestamp={}, URL={}, ResponseBody={}",
                    storeId, orderId, System.currentTimeMillis(), url, objectMapper.writeValueAsString(result));
        }
        return result;
    }
}
