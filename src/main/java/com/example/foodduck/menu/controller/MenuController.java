package com.example.foodduck.menu.controller;

import com.example.foodduck.menu.dto.request.MenuCreateRequest;
import com.example.foodduck.menu.dto.request.MenuUpdateRequest;
import com.example.foodduck.menu.dto.response.MenuCreateResponse;
import com.example.foodduck.menu.dto.response.MenuResponse;
import com.example.foodduck.menu.dto.response.MenuUpdateResponse;
import com.example.foodduck.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("stores/{storeId}/menus")
    public ResponseEntity<MenuCreateResponse> createMenu(
            @PathVariable Long storeId,
            @Valid @RequestBody MenuCreateRequest menuCreateRequest
    ) {
        return new ResponseEntity<>(menuService.createMenu(storeId, menuCreateRequest), HttpStatus.CREATED);
    }

    @GetMapping("stores/{storeId}/menus")
    public ResponseEntity<Page<MenuResponse>> getMenus(
            @PathVariable Long storeId,
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int size,
            @RequestParam (required = false) String menuName,
            @RequestParam (required = false) String category,
            @RequestParam (defaultValue = "createdAt") String sortCondition

    ) {
        return new ResponseEntity<>(menuService.getMenus(storeId, page,size, menuName, category, sortCondition), HttpStatus.OK);
    }

    @GetMapping("/menus/{menuId}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable Long menuId) {
        return new ResponseEntity<>(menuService.getMenu(menuId), HttpStatus.OK);
    }

    @PatchMapping("/menus/{menuId}/update")
    public ResponseEntity<MenuUpdateResponse> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequest menuUpdateRequest
    ) {
        return new ResponseEntity<>(menuService.updateMenu(menuId, menuUpdateRequest), HttpStatus.OK);
    }

    @DeleteMapping("/menus/{menuId}/delete")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
