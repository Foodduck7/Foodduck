package com.example.foodduck.menu.dto.response;

import com.example.foodduck.menu.entity.MenuOption;
import com.example.foodduck.menu.entity.OptionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MenuOptionCreateResponse {

    private final Long id;
    private final Long menuId;
    private final String optionName;
    private final String contents;
    private final int optionPrice;
    private final OptionStatus optionStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public MenuOptionCreateResponse(Long id, Long menuId, String optionName, String contents, int optionPrice, OptionStatus optionStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.menuId = menuId;
        this.optionName = optionName;
        this.contents = contents;
        this.optionPrice = optionPrice;
        this.optionStatus = optionStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MenuOptionCreateResponse toDto(MenuOption menuOption) {
        return MenuOptionCreateResponse.builder()
                .id(menuOption.getId())
                .menuId(menuOption.getMenu().getId())
                .optionName(menuOption.getOptionName())
                .contents(menuOption.getContents())
                .optionPrice(menuOption.getOptionPrice())
                .optionStatus(menuOption.getOptionStatus())
                .createdAt(menuOption.getCreatedAt())
                .updatedAt(menuOption.getUpdatedAt())
                .build();
    }
}
