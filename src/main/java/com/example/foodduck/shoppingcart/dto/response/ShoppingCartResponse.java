package com.example.foodduck.shoppingcart.dto.response;

import java.time.LocalDateTime;

public record ShoppingCartResponse(long id, long storeId, LocalDateTime modifiedAt) {
}
