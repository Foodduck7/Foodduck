package com.example.foodduck.menu.dto.request;

import com.example.foodduck.menu.entity.MenuState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MenuCreateRequest {

    @NotBlank(message = "메뉴 이름을 입력해 주세요.")
    @Size(min = 1, max = 50)
    private String menuName;

    @NotNull(message = "가격을 입력해 주세요.")
    @PositiveOrZero
    private int price;

}
