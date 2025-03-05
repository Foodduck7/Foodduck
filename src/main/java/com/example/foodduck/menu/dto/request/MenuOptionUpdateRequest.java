package com.example.foodduck.menu.dto.request;

import com.example.foodduck.menu.entity.OptionStatus;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MenuOptionUpdateRequest {

    @Size(min = 1, max = 30)
    private String option;

    @Size(min = 1, max = 50)
    private String contents;

    @PositiveOrZero
    private int price;

    private OptionStatus optionStatus;
}
