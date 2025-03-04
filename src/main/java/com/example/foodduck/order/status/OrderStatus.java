package com.example.foodduck.order.status;

/**
 * 주문 상태값 저장 enum
 *
 * @author 이호수
 * @version 주문 상태값 구현
 */
public enum OrderStatus {
    // 요청
    REQUESTED,
    //거절됨
    REJECTED,
    // 배달중
    DELIVERY,
    //삭제됨
    REMOVED;
}
