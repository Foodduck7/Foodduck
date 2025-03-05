package com.example.foodduck.store.controller;

import com.example.foodduck.store.dto.request.NoticeUpdateRequestDto;
import com.example.foodduck.store.dto.request.StoreSaveRequestDto;
import com.example.foodduck.store.dto.request.StoreUpdateRequestDto;
import com.example.foodduck.store.dto.response.NoticeUpdateResponseDto;
import com.example.foodduck.store.dto.response.StoreResponseDto;
import com.example.foodduck.store.dto.response.StoreSaveResponseDto;
import com.example.foodduck.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // Create
    @PostMapping("/stores/{userId}")
    public ResponseEntity<StoreSaveResponseDto> saveStore(
            @PathVariable Long userId,
            @Valid
            @RequestBody
            StoreSaveRequestDto requestDto){

        StoreSaveResponseDto responseDto = storeService.save(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // Reade
    // 단건 : ? -> StoreDetailResponseDto
    // 다건 : ? -> StoreSimpleResponseDto
    @GetMapping("/stores")
    public ResponseEntity<?> getStores(@RequestParam String name) {
        return ResponseEntity.ok(storeService.searchStoresByName(name));
    }

    // Update
    @PatchMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @PathVariable Long storeId,
            @RequestParam Long userId,
            @Valid @RequestBody StoreUpdateRequestDto dto) {

        return ResponseEntity.ok(storeService.update(storeId, userId, dto));
    }
    // noticeUpdate
    @PatchMapping("/stores/{storeId}/notice")
    public ResponseEntity<NoticeUpdateResponseDto> updateStoreNotice(
            @PathVariable Long storeId,
            @RequestParam Long userId,
            @Valid @RequestBody NoticeUpdateRequestDto dto) {
        return ResponseEntity.ok(storeService.updateNotice(storeId, userId, dto.getNoticeContent()));
    }

    // Delete
    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId, @RequestParam Long userId) {
        storeService.delete(storeId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}