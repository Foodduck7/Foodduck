package com.example.foodduck.favorite.service;

import com.example.foodduck.favorite.dto.response.FavoriteResponseDto;

import java.util.List;

public interface FavoriteService {
    void addFavorite(Long userId, Long storeId);
    void removeFavorite(Long userId, Long storeId);
    List<FavoriteResponseDto> getFavoritesByUser(Long userId);
}
