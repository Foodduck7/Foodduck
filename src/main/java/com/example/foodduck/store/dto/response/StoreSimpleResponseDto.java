package com.example.foodduck.store.dto.response;

import com.example.foodduck.store.entity.Store;
import lombok.Getter;

@Getter
public class StoreSimpleResponseDto {
    private final Long id;
    private final String name;

    public StoreSimpleResponseDto(Store store) {
        this.id = store.getId();
        this.name = store.getName();
    }
}
