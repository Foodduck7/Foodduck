package com.example.foodduck.menu.controller;

import com.example.foodduck.menu.dto.request.MenuOptionCreateRequest;
import com.example.foodduck.menu.dto.response.MenuOptionCreateResponse;
import com.example.foodduck.menu.service.MenuOptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
