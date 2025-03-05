package com.example.foodduck.menu.dto.request;

import com.example.foodduck.menu.entity.MenuState;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MenuUpdateRequest {
    @Size(min = 1, max = 50)
    private String menuName;

    @PositiveOrZero
    private int price;

    @Size(min = 1, max = 30)
    private String category;

    private MenuState menuState;


}
