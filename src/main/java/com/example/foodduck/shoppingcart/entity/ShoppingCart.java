package com.example.foodduck.shoppingcart.entity;

import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.shoppingcart.status.ShoppingCartStatus;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "shoppingCart")
@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCartMenu> shoppingCartMenus = new ArrayList<>();

    // 배송비 기본값 3000원
    // TODO: 배송비 값에 대한 설정
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal deliveryFee = new BigDecimal("3000");

    private LocalDateTime modifiedAt;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'EXISTS'")
    @Enumerated(EnumType.STRING)
    private ShoppingCartStatus shoppingCartStatus = ShoppingCartStatus.EXISTS;

    public ShoppingCart(User user, Store store, LocalDateTime modifiedAt) {
        this.user = user;
        this.store = store;
        this.modifiedAt = modifiedAt;

    }

    // 장바구니 메뉴 추가 메서드
    public void addMenu(Menu menu, int quantity) {
        this.shoppingCartMenus.add(new ShoppingCartMenu(this, menu, quantity));
    }

    // 장바구니 메뉴 삭제 메서드
    public void removeMenu(Menu menu) {
        ShoppingCartMenu foundCartMenu = this.shoppingCartMenus.stream()
                .filter(s -> s.getMenu().equals(menu))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("ShoppingCartMenu Not Found"));
        this.shoppingCartMenus.remove(foundCartMenu);
    }

    // 업데이트 시점으로부터 하루 지났는지 여부 리턴 메서드
    public boolean isExpired() {
        return modifiedAt.isBefore(LocalDateTime.now().minusDays(1));
    }

    // 장바구니 soft delete 메서드
    public void delete() {
        this.shoppingCartStatus = ShoppingCartStatus.REMOVED;
    }

    @PreUpdate
    public void updateTimestamp() {
        this.modifiedAt = LocalDateTime.now();
    }
}
