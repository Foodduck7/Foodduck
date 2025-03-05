package com.example.foodduck.store.repository;

import com.example.foodduck.store.entity.Store;
import com.example.foodduck.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByOwner(User owner);
}
