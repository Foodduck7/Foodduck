package com.example.foodduck.shoppingcart.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ShoppingCartCreateRequest {
    @NotNull(message = "메뉴 id값 입력은 필수입니다")
    private long menuId;
    @NotNull(message = "메뉴 수량 입력은 필수입니다")
    private int quantity;
    @NotNull(message = "가게 id값 입력은 필수입니다")
    private long storeId;
}
