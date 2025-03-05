package com.example.foodduck.store.dto.response;

import com.example.foodduck.menu.dto.response.MenuResponse;
import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.store.entity.BreakState;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.entity.StoreState;
import com.example.foodduck.user.dto.response.UserResponse;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StoreWithMenusResponseDto {

    private final Long id;
    private final UserResponse owner;
    private final String name;
    private final int minOrderPrice;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final BreakState breakState;
    private final int likeCount;
    private final int orderCount;
    private final StoreState storeState;
    private final List<MenuResponse> menus;

    public StoreWithMenusResponseDto(Store store, List<Menu> menus) {
        this.id = store.getId();
        this.owner = new UserResponse(store.getOwner());
        this.name = store.getName();
        this.minOrderPrice = store.getMinOrderPrice();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.breakState = store.getBreakState();
        this.likeCount = store.getLikeCount();
        this.orderCount = store.getOrderCount();
        this.storeState = store.getStoreState();
        this.menus = menus.stream()
                .map(MenuResponse::toDto)
                .collect(Collectors.toList());
    }
}
