
package com.example.foodduck.order.controller;

import com.example.foodduck.order.dto.request.OrderCreateRequest;
import com.example.foodduck.order.dto.request.OrderUpdateRequest;
import com.example.foodduck.order.dto.response.OrderGetResponse;
import com.example.foodduck.order.dto.response.OrderResponse;
import com.example.foodduck.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * 주문 기능 제어 기능 수행
 * @author 이호수
 * @version 주문 CRUD 기능 구현
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/shoppingCarts/{id}/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping("/request")
    @ResponseBody
    public ResponseEntity<OrderResponse> createOrder(@PathVariable Long id, @Valid @RequestBody OrderCreateRequest orderCreateRequest) {
        return ResponseEntity.ok(orderService.createOrder(id, orderCreateRequest));
    }

    // 주문 조회
    @GetMapping("/{orderId}")
    @ResponseBody
    public ResponseEntity<OrderGetResponse> findOrder(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(orderService.findOrder(orderId));
    }

    // 주문 상태 변경
    @PutMapping("/status")
    @ResponseBody
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderUpdateRequest orderUpdateRequest) throws BadRequestException {
        return ResponseEntity.ok(orderService.updateOrderState(id, orderUpdateRequest));
    }

    // 주문 취소
    @DeleteMapping("/{orderId}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}