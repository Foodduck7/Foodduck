package com.example.foodduck.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuOptionCreateRequest {

    @NotBlank(message = "메뉴 옵션을 입력해주세요.")
    @Size(min = 1, max = 30)
    private String optionName;

    @NotBlank(message = "옵션 내용을 입력해주세요.")
    @Size(min = 1, max = 50)
    private String contents;

    @NotNull(message = "가격을 입력해주세요.")
    @PositiveOrZero
    private int price;
}
