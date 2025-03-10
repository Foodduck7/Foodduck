package com.example.foodduck.menu.entity;

import com.example.foodduck.common.entity.BaseEntity;
import com.example.foodduck.store.entity.Store;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.foodduck.menu.entity.MenuState.ON_SALE;
import static com.example.foodduck.menu.entity.MenuState.REMOVED;

@Getter
@Entity
@Table(name = "menus")
@NoArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;

    private int price;

    private String category;

    @Enumerated(EnumType.STRING)
    private MenuState menuState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy =  "menu",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<MenuOption> menuOptionList = new ArrayList<>();

    public Menu(Long menuId) {
        this.id = menuId;
    }

    public Menu(String menuName, int price, String category, Store store) {
        this.menuName = menuName;
        this.price = price;
        this.category = category;
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

    public void deleteMenu() {
        this.menuState = REMOVED;
    }

    public void updateMenuCategory(String category) {
        this.category = category;
    }
}
