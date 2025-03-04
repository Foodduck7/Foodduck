package com.example.foodduck.menu.entity;

import com.example.foodduck.common.entity.BaseEntity;
import com.example.foodduck.store.entity.Store;
import jakarta.persistence.*;

import lombok.Getter;

import static com.example.foodduck.menu.entity.MenuState.ON_SALE;

@Getter
@Entity
@Table(name = "menus")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;

    private int price;

    @Enumerated(EnumType.STRING)
    private MenuState menuState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public Menu() {

    }

    public Menu(String menuName, int price, Store store) {
        this.menuName = menuName;
        this.price = price;
        this.menuState = ON_SALE;
        this.store = store;
    }

    public void updateMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updateMenuStatus(MenuState menuStatus) {
        this.menuState = menuStatus;
    }
}
