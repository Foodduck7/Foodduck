package com.example.foodduck.shoppingcart.dto.response;

import com.example.foodduck.shoppingcart.entity.ShoppingCartMenu;
import lombok.Getter;

@Getter
public class ShoppingCartMenuResponse {

    private long id;
    private long menuId;
    private int quantity;

    ShoppingCartMenuResponse(ShoppingCartMenu shoppingCartMenu) {
        this.id = shoppingCartMenu.getId();
        this.menuId = shoppingCartMenu.getMenu().getId();
        this.quantity = shoppingCartMenu.getQuantity();
    }
}
