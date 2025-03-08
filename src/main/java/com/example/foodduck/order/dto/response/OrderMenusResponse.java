package com.example.foodduck.order.dto.response;

import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.order.entity.Order;
import com.example.foodduck.order.entity.OrderMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderMenusResponse {

    private Long id;
    private Long orderId;
    private Long menuId;
    private int quantity;
    private int price;

}
