package com.example.foodduck.menu.controller;

import com.example.foodduck.menu.dto.request.MenuCreateRequest;
import com.example.foodduck.menu.dto.request.MenuUpdateRequest;
import com.example.foodduck.menu.dto.response.MenuCreateResponse;
import com.example.foodduck.menu.dto.response.MenuResponse;
import com.example.foodduck.menu.dto.response.MenuUpdateResponse;
import com.example.foodduck.menu.dto.response.MenuWithOptionResponse;
import com.example.foodduck.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/stores/{storeId}/menus")
    public ResponseEntity<MenuCreateResponse> createMenu(
            @PathVariable Long storeId,
            @Valid @RequestBody MenuCreateRequest menuCreateRequest
    ) {
        return ResponseEntity.ok(menuService.createMenu(storeId, menuCreateRequest));
    }

    @GetMapping("/stores/{storeId}/menus")
    public ResponseEntity<Page<MenuResponse>> getMenus(
            @PathVariable Long storeId,
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int size,
            @RequestParam (required = false) String menuName,
            @RequestParam (required = false) String category,
            @RequestParam (defaultValue = "createdAt") String sortCondition

    ) {
        return ResponseEntity.ok(menuService.getMenus(storeId, page, size, menuName, category, sortCondition));
    }

    @GetMapping("/menus/{menuId}")
    public ResponseEntity<MenuWithOptionResponse> getMenu(@PathVariable Long menuId) {
        return ResponseEntity.ok(menuService.getMenu(menuId));
    }

    @PatchMapping("/menus/{menuId}/update")
    public ResponseEntity<MenuUpdateResponse> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequest menuUpdateRequest
    ) {
        return ResponseEntity.ok(menuService.updateMenu(menuId, menuUpdateRequest));
    }

    @DeleteMapping("/menus/{menuId}/delete")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.noContent().build();
    }
}
