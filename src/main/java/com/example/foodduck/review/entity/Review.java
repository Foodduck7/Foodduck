/*
    Title: Review.java
    Author: yjn33
    Date: 2025/02/28
    GitHub: https://github.com/yjn33

    리뷰 구현을 위한 Review엔터티 입니다.
*/

package com.example.foodduck.review.entity;

import com.example.foodduck.common.entity.BaseEntity;
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
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating",nullable = false)
    private int rating;


    @Column(name = "content", nullable = false)
    private String content;



    // 외래키
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // content 다건 조회
    @OneToMany(mappedBy = "reviews", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> contentList = new ArrayList<>();

    /*
    // 외래키
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store_id;


    //외래키
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu_id;
    */



    public Review(int rating, String content){
        this.rating = rating;
        this.content = content;
    }



}
