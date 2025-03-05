package com.example.foodduck.menu.repository;

import com.example.foodduck.menu.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {
    boolean existsByContents(String contents);
}
