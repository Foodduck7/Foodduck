package com.example.foodduck.menu.dto.response;

import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.entity.MenuState;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MenuUpdateResponse {

    private final Long id;

    private final Long storeId;

    private final String menuName;

    private final int optionPrice;

    private final String category;

    private final MenuState menuState;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public MenuUpdateResponse(Long id, Long storeId, String menuName, int optionPrice, String category, MenuState menuState, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.storeId = storeId;
        this.menuName = menuName;
        this.optionPrice = optionPrice;
        this.category = category;
        this.menuState = menuState;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MenuUpdateResponse toDto(Menu menu) {
        return MenuUpdateResponse.builder()
                .id(menu.getId())
                .storeId(menu.getStore().getId())
                .menuName(menu.getMenuName())
                .optionPrice(menu.getPrice())
                .category(menu.getCategory())
                .menuState(menu.getMenuState())
                .createdAt(menu.getCreatedAt())
                .updatedAt(menu.getUpdatedAt())
                .build();
    }
}
