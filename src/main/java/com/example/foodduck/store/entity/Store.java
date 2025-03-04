package com.example.foodduck.store.entity;

import com.example.foodduck.common.entity.BaseEntity;
import com.example.foodduck.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "stores")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int minOrderPrice;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BreakState breakState;

    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private int orderCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreState storeState = StoreState.ACTIVE;

    public Store(User owner, String name, int minOrderPrice, LocalTime openTime, LocalTime closeTime, BreakState breakState) {
        this.owner = owner;
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.breakState = breakState;
        this.likeCount = 0;
        this.orderCount = 0;
        this.storeState = StoreState.ACTIVE;
    }

    public Store(Long storeId) {
        this.id = storeId;
    }

    public void update(String name, int minOrderPrice, LocalTime openTime, LocalTime closeTime, BreakState breakState) {
        this.name = name;
        this.minOrderPrice = minOrderPrice;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.breakState = breakState;
    }
}
