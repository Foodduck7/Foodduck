package com.example.foodduck.shoppingcart.entity;

import com.example.foodduck.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "shoppingCartMenu")
@Entity
public class ShoppingCartMenu {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart shoppingCart;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private int quantity;

    public ShoppingCartMenu(ShoppingCart shoppingCart, Menu menu, int quantity) {
        this.shoppingCart = shoppingCart;
        this.menu = menu;
        this.quantity = quantity;
        shoppingCart.getShoppingCartMenus().add(this);
    }

}
