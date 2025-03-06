package com.example.foodduck.shoppingcart.repository;

import com.example.foodduck.shoppingcart.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("SELECT s FROM ShoppingCart s JOIN FETCH s.shoppingCartMenus scm JOIN FETCH scm.menu WHERE s.user.id = :userId")
    Optional<ShoppingCart> findByUserIdWithMenu(@Param("userId") long userId);

}