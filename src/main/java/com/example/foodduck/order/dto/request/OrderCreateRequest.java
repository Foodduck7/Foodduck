package com.example.foodduck.order.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 주문 생성 요청 전달 DTO
 * @author 이호수
 * @version 사용자와 메뉴 id 를 필드로 가짐
 * getter 와 생성자를 가짐
*/
@Getter
public class OrderCreateRequest {
    @NotNull(message = "사용자 id값 입력은 필수입니다")
    private long userId;
    @NotNull(message = "메뉴 id값 입력은 필수입니다")
    private long menuId;
    @NotNull(message = "가게 id값 입력은 필수입니다")
    private long storeId;
}
