package com.example.foodduck.favorite.repository;

import com.example.foodduck.favorite.entity.Favorite;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserAndStore(User user, Store store);
    List<Favorite> findAllByUser(User user);
}
