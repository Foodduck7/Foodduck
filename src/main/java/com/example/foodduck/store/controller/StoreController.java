package com.example.foodduck.store.controller;

import com.example.foodduck.store.dto.request.StoreSaveRequestDto;
import com.example.foodduck.store.dto.response.StoreResponseDto;
import com.example.foodduck.store.repository.StoreRepository;
import com.example.foodduck.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreRepository storeRepository;
    private final StoreService storeService;

    // Create
    @PostMapping("/stores")
    public ResponseEntity<StoreResponseDto> saveStore(
            @RequestBody Long userId,
            @Valid
            @RequestBody
            StoreSaveRequestDto requestDto){

        StoreResponseDto responseDto = storeService.save(userId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // Reade
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> getStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.findById(storeId));
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponseDto>> getAllStores() {
        return ResponseEntity.ok(storeService.findAll());
    }

    // Update
    @PatchMapping("/stores/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @PathVariable Long storeId,
            @RequestParam Long userId,
            @Valid @RequestBody StoreSaveRequestDto requestDto) {

        return ResponseEntity.ok(storeService.update(storeId, userId, requestDto));
    }

    // Delete
    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId, @RequestParam Long userId) {
        storeService.delete(storeId, userId);
        return ResponseEntity.noContent().build();
    }
}
