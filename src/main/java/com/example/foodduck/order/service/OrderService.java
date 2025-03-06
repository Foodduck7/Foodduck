package com.example.foodduck.order.service;

import com.example.foodduck.exception.custom.MinimumOrderAmountException;
import com.example.foodduck.exception.custom.OutOfOrderTimeException;
import com.example.foodduck.order.dto.request.OrderCreateRequest;
import com.example.foodduck.order.dto.request.OrderUpdateRequest;
import com.example.foodduck.order.dto.response.OrderGetResponse;
import com.example.foodduck.order.dto.response.OrderResponse;
import com.example.foodduck.order.entity.Order;
import com.example.foodduck.order.entity.OrderMenu;
import com.example.foodduck.order.repository.OrderRepository;
import com.example.foodduck.order.status.OrderStatus;
import com.example.foodduck.order.status.OwnerApprovalStatus;
import com.example.foodduck.shoppingcart.entity.ShoppingCart;
import com.example.foodduck.shoppingcart.repository.ShoppingCartRepository;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.store.entity.StoreState;
import com.example.foodduck.user.entity.User;
import com.example.foodduck.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 주문 기능 로직 실행
 * @author 이호수
 * @version 주문 CRUD 비즈니스 로직 구현
 */
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    // 주문 생성 메서드
    @Transactional
    public OrderResponse createOrder(Long id, OrderCreateRequest orderCreateRequest) {
        // 장바구니 조회
        ShoppingCart foundShoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shopping Cart Not Found"));
        // 가게 오픈 상태 아닐 경우 예외처리
        Store foundStore = foundShoppingCart.getStore();
        if (foundStore.getStoreState().equals(StoreState.INACTIVE)) {
            throw new OutOfOrderTimeException("Not Currently Available For Order");
        }
        List<OrderMenu> orderMenus = foundShoppingCart.getShoppingCartMenus().stream()
                .map(scm -> new OrderMenu(scm.getMenu(), scm.getQuantity()))
                .toList();
        // 주문 총 금액
        BigDecimal totalAmount = orderMenus.stream()
                .map(om -> BigDecimal.valueOf(om.getMenu().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 가게 최소 주문 금액 넘지 않을 경우 예외처리
        if ((BigDecimal.valueOf(foundStore.getMinOrderPrice())).compareTo(totalAmount) > 0) {
            throw new MinimumOrderAmountException("Minimum Order Amount Should Be More Than: " + foundStore.getMinOrderPrice());
        }
        // 사용자 조회
        User foundUser = userRepository.findById(orderCreateRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
        Order order = new Order(foundUser, orderMenus, foundStore, OrderStatus.REQUESTED);
        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder.getId(), foundStore.getId(), savedOrder.getOrderStatus());
    }

    // 주문 조회 메서드
    @Transactional(readOnly = true)
    public OrderGetResponse findOrder(Long id) {
        Order foundOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
        // 주문 총 금액
        BigDecimal totalAmount = foundOrder.getOrderMenus().stream()
                .map(om -> BigDecimal.valueOf(om.getMenu().getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new OrderGetResponse(foundOrder.getId(), foundOrder.getStore().getId(), foundOrder.getOrderStatus(), foundOrder.getOrderMenus(), totalAmount);
    }

    // 주문 상태 수정 메서드
    // 주문 요청 -> 사장님 취소/수락 -> 주문 상태 변경
    @Transactional
    public OrderResponse updateOrderState(Long id, OrderUpdateRequest orderUpdateRequest) throws BadRequestException {
        Order foundOrder = orderRepository.findById(orderUpdateRequest.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
        // 사장님 주문 취소 또는 수락
        foundOrder.updateOrderState(
                String.valueOf(OwnerApprovalStatus.ACCEPT).equalsIgnoreCase(orderUpdateRequest.getOwnerApprovalStatus())
                        ? OrderStatus.DELIVERY : OrderStatus.REJECTED);
        if (OwnerApprovalStatus.of(orderUpdateRequest.getOwnerApprovalStatus()).equals(OwnerApprovalStatus.ACCEPT)) {
            ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("ShoppingCart Not Found"));
            // 주문되었으므로 장바구니 삭제
            shoppingCart.delete();
        }
        Order savedOrder = orderRepository.save(foundOrder);
        return new OrderResponse(savedOrder.getId(), savedOrder.getStore().getId(), savedOrder.getOrderStatus());
    }

    // 주문 취소 메서드
    @Transactional
    public void deleteOrder(Long orderId) {
        Order foundorder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
        foundorder.deleteOrder();
    }
}
