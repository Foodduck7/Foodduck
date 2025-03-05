package com.example.foodduck.menu.repository;

import com.example.foodduck.menu.entity.Menu;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m " +
            "WHERE m.store.id = :storeId " +
            "AND (:menuName IS NULL OR m.menuName LIKE CONCAT('%', :menuName, '%')) " +
            "AND (:category IS NULL OR m.category = :category)")
    Page<Menu> findAllByStoreId(
            Long storeId,
            @Param("menuName") String menuName,
            @Param("category") String category,
            Pageable pageable);

    boolean existsByMenuName(String menuName);
}
