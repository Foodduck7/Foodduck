package com.example.foodduck.order.entity;

import com.example.foodduck.common.entity.BaseEntity;
import com.example.foodduck.order.status.OrderStatus;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 주문 정보를 저장하는 entity
 * @author 이호수
 * @version 주문 id, 메뉴, 사용자, 주문 상태를 필드로 가짐
 * getter 와 기본 생성자를 가짐
 * 주문 상태 업데이트 메서드를 가짐
 */
@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    // 주문 상태: 기본값 요청 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.REQUESTED;

    // 장바구니 메뉴 추가 메서드
    public void setMenus(List<OrderMenu> orderMenus) {
        this.orderMenus = orderMenus;
    }

    public Order(User user, Store store, OrderStatus orderStatus) {
        this.user = user;
        this.store = store;
        this.orderStatus = orderStatus;
    }

    // 주문 상태 업데이트 메서드
    public void updateOrderState(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    // 주문 soft delete
    // 주문 상태 삭제됨으로 업데이트
    public void deleteOrder() {
        this.orderStatus = OrderStatus.REMOVED;
    }

}
