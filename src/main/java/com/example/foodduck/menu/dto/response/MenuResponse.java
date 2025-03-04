package com.example.foodduck.menu.dto.response;

import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.entity.MenuState;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MenuResponse {
    private final Long id;

    private final Long storeId;

    private final String menuName;

    private final int price;

    private final MenuState menuState;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public MenuResponse(Long id, Long storeId, String menuName, int price, MenuState menuState, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.storeId = storeId;
        this.menuName = menuName;
        this.price = price;
        this.menuState = menuState;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MenuResponse toDto(Menu menu) {
        return new MenuResponse (
                menu.getId(),
                menu.getStore().getId(),
                menu.getMenuName(),
                menu.getPrice(),
                menu.getMenuState(),
                menu.getCreatedAt(),
                menu.getUpdatedAt()
        );
    }
}
