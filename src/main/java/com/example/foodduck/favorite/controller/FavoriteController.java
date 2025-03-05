package com.example.foodduck.favorite.controller;

import com.example.foodduck.favorite.dto.response.FavoriteResponseDto;
import com.example.foodduck.favorite.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/favorites/{userId}/{storeId}")
    public ResponseEntity<String> addFavorite(
            @PathVariable Long userId,
            @PathVariable Long storeId) {
        favoriteService.addFavorite(userId, storeId);
        return ResponseEntity.ok("Favorite added successfully");
    }

    @GetMapping("/favorites/{userId}")
    public ResponseEntity<List<FavoriteResponseDto>> getFavorites(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUser(userId));
    }

    @DeleteMapping("/favorites/{userId}/{storeId}")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long userId,
            @PathVariable Long storeId) {
        favoriteService.removeFavorite(userId, storeId);
        return ResponseEntity.noContent().build();
    }

}
