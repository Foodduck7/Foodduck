/*
    Title: Review.java
    Author: yjn33
    Date: 2025/02/28
    GitHub: https://github.com/yjn33

    리뷰 구현을 위한 Review엔터티 입니다.
*/

package com.example.foodduck.review.entity;

import com.example.foodduck.common.entity.BaseEntity;
import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.order.entity.Order;
import com.example.foodduck.store.entity.Store;
import com.example.foodduck.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;


    public Review(int rating, String content, Order order){
        this.rating = rating;
        this.content = content;
        this.order = order;
    }

}
