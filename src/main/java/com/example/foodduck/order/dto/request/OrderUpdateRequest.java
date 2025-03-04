package com.example.foodduck.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


/**
 * 주문 상태를 업데이트 하는 요청 전달 DTO
 * @author 이호수
 * @version 주문 id 값과 사장님 주문 응답을 필드로 가짐
 * 생성자와 getter 를 가짐
 */
@Getter
public class OrderUpdateRequest {
    @NotNull(message = "주문 id값 입력은 필수입니다")
    private long orderId;
    @NotBlank(message = "사장님 승인 상태값 입력은 필수입니다")
    private String ownerApprovalStatus;
}
