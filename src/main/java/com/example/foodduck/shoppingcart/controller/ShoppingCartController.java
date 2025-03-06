package com.example.foodduck.shoppingcart.controller;

import com.example.foodduck.shoppingcart.dto.request.ShoppingCartAddRequest;
import com.example.foodduck.shoppingcart.dto.request.ShoppingCartCreateRequest;
import com.example.foodduck.shoppingcart.dto.response.ShoppingCartGetResponse;
import com.example.foodduck.shoppingcart.dto.response.ShoppingCartResponse;
import com.example.foodduck.shoppingcart.service.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shoppingCarts")
public class ShoppingCartController {

    private final ShoppingCartService shoppingcartService;

    // 쇼핑카트 생성(메뉴 쇼핑카트에 추가)
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<ShoppingCartResponse> createShoppingCart(@Valid @RequestBody ShoppingCartCreateRequest shoppingCartCreateRequest) {
        return ResponseEntity.ok(shoppingcartService.createShoppingCart(shoppingCartCreateRequest));
    }

    // 쇼핑카트 조회
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ShoppingCartGetResponse> getShoppingCart(@PathVariable long id) {
        return ResponseEntity.ok(shoppingcartService.findShoppingCart(id));
    }

    // 쇼핑카트 메뉴 추가
    @PutMapping("/{id}/add")
    @ResponseBody
    public ResponseEntity<ShoppingCartResponse> addMenuToShoppingCart(@PathVariable long id, @Valid @RequestBody ShoppingCartAddRequest shoppingCartAddRequest) {
        return ResponseEntity.ok(shoppingcartService.addMenuToShoppingCart(id, shoppingCartAddRequest));
    }

    // 쇼핑카트 메뉴 삭제
    // 쇼핑카트 내 메뉴 0개 -> 쇼핑카트 삭제
    @PutMapping("/{id}/remove")
    @ResponseBody
    public ResponseEntity<ShoppingCartResponse> removeMenuToShoppingCart(@PathVariable long id, @RequestParam long menuId) {
        return ResponseEntity.ok(shoppingcartService.removeMenuToShoppingCart(id, menuId));
    }
}
