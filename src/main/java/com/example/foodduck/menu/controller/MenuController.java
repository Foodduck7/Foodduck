package com.example.foodduck.menu.controller;

import com.example.foodduck.menu.dto.request.MenuCreateRequest;
import com.example.foodduck.menu.dto.request.MenuUpdateRequest;
import com.example.foodduck.menu.dto.response.MenuCreateResponse;
import com.example.foodduck.menu.dto.response.MenuUpdateResponse;
import com.example.foodduck.menu.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PatchMapping("menus/{menuId}/update")
    public ResponseEntity<MenuUpdateResponse> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequest menuUpdateRequest,
            HttpServletRequest servletRequest
    ) {
        return new ResponseEntity<>(menuService.updateMenu(servletRequest, menuId, menuUpdateRequest), HttpStatus.OK);
    }
}
