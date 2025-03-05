package com.example.foodduck.order.dto.response;

import com.example.foodduck.order.status.OrderStatus;

/**
 * client 로 로직 수행 응답 값 전달 DTO record
 * @author 이호수
 * @param id
 * @param orderStatus
 */
public record OrderResponse(long id, long storeId, OrderStatus orderStatus) {
}
