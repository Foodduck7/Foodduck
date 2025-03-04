package com.example.foodduck.menu.controller;

import com.example.foodduck.menu.dto.request.MenuCreateRequest;
import com.example.foodduck.menu.dto.request.MenuUpdateRequest;
import com.example.foodduck.menu.dto.response.MenuCreateResponse;
import com.example.foodduck.menu.dto.response.MenuResponse;
import com.example.foodduck.menu.dto.response.MenuUpdateResponse;
import com.example.foodduck.menu.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/menus/{storeId}")
    public ResponseEntity<MenuCreateResponse> createMenu(
            @PathVariable Long storeId,
            @Valid @RequestBody MenuCreateRequest menuCreateRequest,
            HttpServletRequest servletRequest
    ) {

        return new ResponseEntity<>(menuService.createMenu(servletRequest, storeId, menuCreateRequest), HttpStatus.CREATED);
    }

    @GetMapping("/menus")
    public ResponseEntity<Page<MenuResponse>> getMenus(
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int size

    ) {
        return new ResponseEntity<>(menuService.getMenus(page,size), HttpStatus.OK);
    }

    @GetMapping("/menus/{menuId}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable Long menuId) {
        return new ResponseEntity<>(menuService.getMenu(menuId), HttpStatus.OK);
    }

    @PatchMapping("/menus/{menuId}/update")
    public ResponseEntity<MenuUpdateResponse> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequest menuUpdateRequest,
            HttpServletRequest servletRequest
    ) {
        return new ResponseEntity<>(menuService.updateMenu(servletRequest, menuId, menuUpdateRequest), HttpStatus.OK);
    }

    @DeleteMapping("/menus/{menuId}/delete")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long menuId,
            HttpServletRequest servletRequest
    ) {
        menuService.deleteMenu(servletRequest, menuId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
