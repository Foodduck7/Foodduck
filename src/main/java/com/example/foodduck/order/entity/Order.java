package com.example.foodduck.order.entity;

import com.example.foodduck.common.entity.BaseEntity;
import com.example.foodduck.order.status.OrderStatus;
import com.example.foodduck.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

//    @JoinColumn(name = "menu_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Menu menu;

    // 주문 상태: 기본값 요청 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.REQUESTED;

    // 주문 상태 업데이트 메서드
    public void updateOrderState(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

}
