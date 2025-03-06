package com.example.foodduck.shoppingcart.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShoppingCartAddRequest {
    @NotNull(message = "메뉴 id값 입력은 필수입니다")
    private long menuId;
    @NotNull(message = "수량값 입력은 필수입니다")
    private int quantity;
}
