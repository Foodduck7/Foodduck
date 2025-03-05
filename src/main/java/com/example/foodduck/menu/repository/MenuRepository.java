package com.example.foodduck.menu.repository;

import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStore(Store store);

    @EntityGraph(attributePaths = {"store"})
    @Query("SELECT m FROM Menu m " +
            "WHERE m.store.id = :storeId " +
            "AND (:menuName IS NULL OR m.menuName LIKE CONCAT('%', :menuName, '%')) " +
            "AND (:category IS NULL OR m.category = :category)" +
            "AND m.menuState != 'REMOVED'" )
    Page<Menu> findAllByStoreId(
            Long storeId,
            @Param("menuName") String menuName,
            @Param("category") String category,
            Pageable pageable);

    boolean existsByMenuName(String menuName);
}
