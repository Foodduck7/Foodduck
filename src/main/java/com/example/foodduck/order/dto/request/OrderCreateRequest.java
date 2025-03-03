package com.example.foodduck.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 주문 생성 요청 전달 DTO
 * @author 이호수
 * @version 사용자와 메뉴 id 를 필드로 가짐
 * getter 와 생성자를 가짐
*/
@Getter
@RequiredArgsConstructor
public class OrderCreateRequest {
    @NotBlank(message = "사용자 id값 입력은 필수입니다")
    Long userId;
    @NotBlank(message = "메뉴 id값 입력은 필수입니다")
    Long menuId;
}
