package com.example.foodduck.store.dto.request;

import com.example.foodduck.store.entity.BreakState;
import com.example.foodduck.store.entity.StoreState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StoreUpdateRequestDto {

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String name;

    @NotNull(message = "최소 주문 가격은 필수입니다.")
    @Min(value = 0, message = "최소 주문 가격은 0 이상이어야 합니다.")
    private int minOrderPrice;

    @NotNull(message = "오픈 시간은 필수입니다.")
    private LocalTime openTime;

    @NotNull(message = "마감 시간은 필수입니다.")
    private LocalTime closeTime;

    @NotNull(message = "breakState 필수입니다.")
    private BreakState breakState;

    @NotNull(message = "storeState 필수입니다.")
    private StoreState storeState;
}
