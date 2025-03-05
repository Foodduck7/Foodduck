package com.example.foodduck.favorite.service;

import com.example.foodduck.favorite.dto.response.FavoriteResponseDto;
import com.example.foodduck.favorite.entity.Favorite;
import com.example.foodduck.favorite.repository.FavoriteRepository;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.repository.StoreRepository;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository; // userId -> User 변환 위해 필요

    @Override
    @Transactional
    public void addFavorite(Long userId, Long storeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        if (favoriteRepository.findByUserAndStore(user, store).isPresent()) {
            throw new RuntimeException("Already favorited");
        }

        Favorite favorite = new Favorite(user, store);
        favoriteRepository.save(favorite);
        store.setLikeCount(store.getLikeCount() + 1);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long storeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        Favorite favorite = favoriteRepository.findByUserAndStore(user, store)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        favoriteRepository.delete(favorite);

        if (store.getLikeCount() > 0) {
            store.setLikeCount(store.getLikeCount() - 1);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteResponseDto> getFavoritesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Favorite> favorites = favoriteRepository.findAllByUser(user);
        return favorites.stream()
                .map(fav -> new FavoriteResponseDto(fav.getId(), fav.getStore().getId()))
                .collect(Collectors.toList());
    }
}
