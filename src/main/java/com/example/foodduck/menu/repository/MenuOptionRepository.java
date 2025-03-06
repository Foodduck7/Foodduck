package com.example.foodduck.menu.repository;

import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.entity.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {
    boolean existsByContents(String contents);

    List<MenuOption> findAllByMenuId(Long menuId);
}
