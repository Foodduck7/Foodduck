package com.example.foodduck.order.entity;

import com.example.foodduck.common.entity.BaseEntity;
import com.example.foodduck.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_menus")
@NoArgsConstructor
public class OrderMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private int quantity;

    private int price;

    public OrderMenu(Menu menu, int quantity) {
        this.menu = menu;
        this.quantity = quantity;
        this.price = menu.getPrice() * quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
