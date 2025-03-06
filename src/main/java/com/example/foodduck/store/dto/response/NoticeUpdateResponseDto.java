package com.example.foodduck.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeUpdateResponseDto {
    private final Long storeId;
    private final String noticeContent;
}
