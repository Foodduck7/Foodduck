package com.example.foodduck.store.dto.response;

import com.example.foodduck.store.entity.BreakState;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.entity.StoreState;
import com.example.foodduck.user.dto.response.UserResponse;
import com.example.foodduck.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class StoreResponseDto {

    private Long id;
    private UserResponse owner;
    private String name;
    private int minOrderPrice;
    private LocalTime openTime;
    private LocalTime closeTime;
    private BreakState breakState;
    private int likeCount;
    private int orderCount;
    private StoreState storeState;

    public StoreResponseDto(Store store) {
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
    }
}