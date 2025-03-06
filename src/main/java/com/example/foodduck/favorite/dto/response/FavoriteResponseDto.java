package com.example.foodduck.favorite.dto.response;

import lombok.Getter;

@Getter
public class FavoriteResponseDto {
    private Long favoriteId;
    private Long storeId;

    public FavoriteResponseDto(Long favoriteId, Long storeId) {
        this.favoriteId = favoriteId;
        this.storeId = storeId;
    }
}
