package com.example.foodduck.shoppingcart.dto.response;

import com.example.foodduck.shoppingcart.entity.ShoppingCart;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ShoppingCartGetResponse {

    private long id;
    private List<ShoppingCartMenuResponse> shoppingCartMenus;
    private BigDecimal deliveryFee;
    private BigDecimal totalAmount;

    public ShoppingCartGetResponse(ShoppingCart shoppingCart, BigDecimal totalAmount, BigDecimal deliveryFee) {
        this.id = shoppingCart.getId();
        this.deliveryFee = deliveryFee;
        this.totalAmount = totalAmount;
        this.shoppingCartMenus = shoppingCart.getShoppingCartMenus().stream()
                .map(ShoppingCartMenuResponse::new)
                .collect(Collectors.toList());
    }

}
