package com.example.foodduck.menu.controller;

import com.example.foodduck.menu.dto.request.MenuOptionCreateRequest;
import com.example.foodduck.menu.dto.request.MenuOptionUpdateRequest;
import com.example.foodduck.menu.dto.response.MenuOptionUpdateResponse;
import com.example.foodduck.menu.dto.response.MenuOptionCreateResponse;
import com.example.foodduck.menu.service.MenuOptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuOptionController {
    private final MenuOptionService menuOptionService;

    @PostMapping("/menus/{menuId}/options")
    public ResponseEntity<MenuOptionCreateResponse> createMenuOption (
            @PathVariable Long menuId,
            @Valid @RequestBody MenuOptionCreateRequest menuOptionCreateRequest
    ) {
        return ResponseEntity.ok(menuOptionService.createMenuOption(menuId, menuOptionCreateRequest));
    }

    @PatchMapping("/menus/options/{optionId}/update")
    public ResponseEntity<MenuOptionUpdateResponse> updateMenuOption (
            @PathVariable Long optionId,
            @Valid @RequestBody MenuOptionUpdateRequest menuOptionUpdateRequest
    ) {
        return ResponseEntity.ok(menuOptionService.updateMenuOption(optionId, menuOptionUpdateRequest));
    }

    @DeleteMapping("menus/options/{optionId}/delete")
    public ResponseEntity<Void> deleteMenuOption(@PathVariable Long optionId) {
        menuOptionService.deleteMenuOption(optionId);

        return ResponseEntity.noContent().build();
    }


}
