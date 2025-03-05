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

    // content 다건 조회
    //@OneToMany(mappedBy = "reviews", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    //List<Review> contentList = new ArrayList<>();



    /*
        외래키
        회원ID
    */
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    /*
        외래키
        가게ID
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;


    /*
        외래키
        메뉴ID
    */
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;




    public Review(int rating, String content){
        this.rating = rating;
        this.content = content;
    }



}
