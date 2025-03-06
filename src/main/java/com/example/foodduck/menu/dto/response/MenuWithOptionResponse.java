package com.example.foodduck.menu.dto.response;

import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.menu.entity.MenuOption;
import com.example.foodduck.menu.entity.MenuState;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class MenuWithOptionResponse {

    private final Long id;
    private final Long storeId;
    private final String menuName;
    private final int price;
    private final MenuState menuState;
    private final List<MenuOptionCreateResponse> menuOptionList;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static MenuWithOptionResponse toDto(Menu menu, List<MenuOption> menuOptionList) {

        List<MenuOptionCreateResponse> menuOptions = menuOptionList.stream()
                .map(MenuOptionCreateResponse::toDto)
                .collect(Collectors.toList());

        return MenuWithOptionResponse.builder()
                .id(menu.getId())
                .storeId(menu.getStore().getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuState(menu.getMenuState())
                .menuOptionList(menuOptions)
                .createdAt(menu.getCreatedAt())
                .updatedAt(menu.getUpdatedAt())
                .build();
    }
}
