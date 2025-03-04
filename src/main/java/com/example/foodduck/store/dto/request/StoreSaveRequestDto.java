package com.example.foodduck.store.dto.request;

import com.example.foodduck.store.entity.BreakState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreSaveRequestDto {

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String name;

    @Min(value = 0, message = "최소 주문 금액은 0원 이상이어야 합니다.")
    private int minOrderPrice;

    @NotNull(message = "오픈 시간은 필수입니다.")
    private LocalTime openTime;

    @NotNull(message = "마감 시간은 필수입니다.")
    private LocalTime closeTime;

    @NotNull(message = "브레이크 타임은 필수입니다.")
    private BreakState breakState;

}