/*
    Title: Review.java
    Author: yjn33
    Date: 2025/02/28
    GitHub: https://github.com/yjn33

    리뷰 구현을 위한 Review엔터티 입니다.
*/

package com.example.foodduck.review.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;


@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor
public class Review {

    @Id
    @Column(name = "review_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger reviewId;

    @Column(nullable = false)
    private int rating;


    @Column(name = "content", nullable = false)
    private String content;



    public Review(int rating, String content){
        this.rating = rating;
        this.content = content;
    }



}
