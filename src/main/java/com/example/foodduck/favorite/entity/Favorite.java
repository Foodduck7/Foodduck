package com.example.foodduck.favorite.entity;

import com.example.foodduck.common.entity.BaseEntity;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "store_id"})
})
public class Favorite extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Favorite(User user, Store store) {
        this.user = user;
        this.store = store;
    }
}