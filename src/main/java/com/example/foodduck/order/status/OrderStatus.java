package com.example.foodduck.order.status;

import org.apache.coyote.BadRequestException;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * 주문 상태값 저장 enum
 * @author 이호수
 * @version 주문 상태값 구현
 */
public enum OrderStatus {
    // 요청
    REQUESTED,
    //거절됨
    REJECTED,
    // 배달중
    DELIVERY;

    /*
    쓰임 없음
    // 입력받은 string 과 일치하는 enum 값 리턴
    public static OrderStatus of(String orderStatus) throws BadRequestException {
        return Arrays.stream(OrderStatus.values())
                .filter(o -> String.valueOf(o).equalsIgnoreCase(orderStatus))
                .findFirst()
                .orElseThrow(()
                        -> new BadRequestException(MessageFormat.format("Invalid Order Status: {0}", orderStatus)));
    }
     */
}
