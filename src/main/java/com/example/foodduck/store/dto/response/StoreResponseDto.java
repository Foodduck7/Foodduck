package com.example.foodduck.store.dto.response;

import com.example.foodduck.store.entity.BreakState;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.entity.StoreState;
import com.example.foodduck.user.entity.User;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreResponseDto {

    private final Long id;
    private final User owner;
    private final String name;
    private final int minOrderPrice;
    private final LocalTime openTime;
    private final LocalTime closeTime;
    private final BreakState breakState;
    private final int likeCount;
    private final int orderCount;
    private final StoreState storeState;

    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.owner = store.getOwner();
        this.name = store.getName();
        this.minOrderPrice = store.getMinOrderPrice();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.breakState = store.getBreakState();
        this.likeCount = store.getLikeCount();
        this.orderCount = store.getOrderCount();
        this.storeState = store.getStoreState();
    }
}
