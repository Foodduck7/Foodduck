package com.example.foodduck.order.dto.response;

import com.example.foodduck.order.entity.OrderMenu;
import com.example.foodduck.order.status.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class OrderGetResponse {
    private Long id;
    private Long storeId;
    private OrderStatus orderStatus;
    private List<OrderMenu> orderMenus;
    private BigDecimal totalAmount;
}
