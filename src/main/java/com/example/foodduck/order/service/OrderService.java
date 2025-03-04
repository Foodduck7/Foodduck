package com.example.foodduck.order.service;

import com.example.foodduck.exception.MinimumOrderAmountException;
import com.example.foodduck.exception.OutOfOrderTimeException;
import com.example.foodduck.order.dto.request.OrderCreateRequest;
import com.example.foodduck.order.dto.request.OrderUpdateRequest;
import com.example.foodduck.order.dto.response.OrderResponse;
import com.example.foodduck.order.entity.Order;
import com.example.foodduck.order.repository.OrderRepository;
import com.example.foodduck.order.status.OrderStatus;
import com.example.foodduck.order.status.OwnerApprovalStatus;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.entity.StoreState;
import com.example.foodduck.store.repository.StoreRepository;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;

/**
 * 주문 기능 로직 실행
 * @author 이호수
 * @version 주문 CRUD 비즈니스 로직 구현
 * 다른 도메인 관련 내용 주석 처리
 * 테스트 수행 필요
 */
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    // 주문 생성 메서드
    @Transactional
    public OrderResponse createOrder(OrderCreateRequest orderCreateRequest) {
        // 메뉴 조회
        Menu foundMenu = menuRepository.findById(orderCreateRequest.getMenuId())
                .orElseThrow(() -> new EntityNotFoundException("Menu Not Found"));
        // 가게 오픈 상태 아닐 경우 예외처리
        Store foundStore = storeRepository.findById(foundMenu.getStore().getId());
        if (foundStore.getStoreState().equals(StoreState.INACTIVE)) {
            throw new OutOfOrderTimeException("Not Currently Available For Order");
        }
        // 가게 최소 주문 금액 넘지 않을 경우 예외처리
        if (foundStore.getMinOrderPrice() > foundMenu.getPrice()) {
            throw new MinimumOrderAmountException("Minimum Order Amount Should Be More Than: " + foundStore.getMinOrderPrice());
        }
        // 사용자 조회
        User foundUser = userRepository.findById(orderCreateRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        Order order = new Order(foundUser, foundMenu, OrderStatus.REQUESTED);
        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderStatus());
    }

    // 주문 조회 메서드
    @Transactional(readOnly = true)
    public OrderResponse findOrder(Long id) {
        Order foundOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
        return new OrderResponse(foundOrder.getId(), foundOrder.getOrderStatus());
    }

    // 주문 상태 수정 메서드
    // 주문 요청 -> 사장님 취소/수락 -> 수락될 경우 메뉴 상태 변경
    @Transactional
    public OrderResponse updateOrderState(OrderUpdateRequest orderUpdateRequest) throws BadRequestException {
        Order foundOrder = orderRepository.findById(orderUpdateRequest.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
        // 사장님 주문 취소
        if (OwnerApprovalStatus.of(orderUpdateRequest.getOwnerApprovalStatus()).equals(OwnerApprovalStatus.REJECT)) {
            foundOrder.updateOrderState(OrderStatus.REJECTED);
            Order savedOrder = orderRepository.save(foundOrder);
            return new OrderResponse(savedOrder.getId(), savedOrder.getOrderStatus());
        }
        // 사장님 주문 수락
        foundOrder.updateOrderState(OrderStatus.DELIVERY);
        Order savedOrder = orderRepository.save(foundOrder);
        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderStatus());
    }

    // 주문 취소 메서드
    @Transactional
    public void deleteOrder(long id) {
        Order foundorder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
        foundorder.deleteOrder();
    }
}
