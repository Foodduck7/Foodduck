
package com.example.foodduck.order.controller;

import com.example.foodduck.order.dto.request.OrderCreateRequest;
import com.example.foodduck.order.dto.request.OrderUpdateRequest;
import com.example.foodduck.order.dto.response.OrderResponse;
import com.example.foodduck.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 주문 기능 제어 기능 수행
 * @author 이호수
 * @version 주문 CRUD 기능 구현
 * 다른 도메인 관련 내용 주석 처리
 * 사장님 주문 수락/최소 기능 구현 필요
 * 예외사항 처리 구현 필요
 * 테스트 수행 필요
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping("/request")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest orderCreateRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderCreateRequest));
    }

    // 주문 조회
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findOrder(id));
    }

    // 주문 상태 변경
    @PutMapping("/status")
    public ResponseEntity<OrderResponse> updateOrder(@Valid @RequestBody OrderUpdateRequest orderUpdateRequest) throws BadRequestException {
        return ResponseEntity.ok(orderService.updateOrderState(orderUpdateRequest));
    }

    // 주문 취소
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}