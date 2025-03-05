package com.example.foodduck.menu.repository;

import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStore(Store store);
}
