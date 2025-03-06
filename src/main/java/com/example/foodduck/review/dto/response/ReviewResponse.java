/*
    Title: ReviewResponse.java
    Author: yjn33
    Date: 2025/02/28
    GitHub: https://github.com/yjn33

    Review 조회 응답을 위한 dto 입니다
*/


package com.example.foodduck.review.dto.response;

import java.math.BigInteger;

import com.example.foodduck.review.entity.Review;
import lombok.Getter;

@Getter
public class ReviewResponse {

    private final Long id;
    private final int rating;
    private final String content;


    public ReviewResponse(Review review){
        this.id = review.getId();
        this.rating = review.getRating();
        this.content = review.getContent();
    }

}
