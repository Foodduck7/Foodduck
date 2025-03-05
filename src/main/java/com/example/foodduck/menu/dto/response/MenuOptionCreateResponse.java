package com.example.foodduck.menu.dto.response;

import com.example.foodduck.menu.entity.MenuOption;
import com.example.foodduck.menu.entity.OptionStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MenuOptionCreateResponse {
    private final Long id;

    private final Long menuId;

    private final String optionName;

    private final String contents;

    private final int price;

    private final OptionStatus optionStatus;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public MenuOptionCreateResponse(Long id, Long menuId, String optionName, String contents, int price, OptionStatus optionStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.menuId = menuId;
        this.optionName = optionName;
        this.contents = contents;
        this.price = price;
        this.optionStatus = optionStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //TODO: Builder로 변경
    public static MenuOptionCreateResponse toDto(MenuOption menuOption) {
        return new MenuOptionCreateResponse(
                menuOption.getId(),
                menuOption.getMenu().getId(),
                menuOption.getOptionName(),
                menuOption.getContents(),
                menuOption.getOptionPrice(),
                menuOption.getOptionStatus(),
                menuOption.getCreatedAt(),
                menuOption.getUpdatedAt()
        );
    }
}
