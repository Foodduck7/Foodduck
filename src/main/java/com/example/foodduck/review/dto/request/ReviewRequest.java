/*
    Title: ReviewRequest.java
    Author: yjn33
    Date: 2025/02/28
    GitHub: https://github.com/yjn33

    Review 등록을 위한 dto 입니다
*/


package com.example.foodduck.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;



@NoArgsConstructor
public class ReviewRequest {

    @NotBlank(message = "평점을 입력해 주세요")
    private int rating;

    @NotBlank(message = "리뷰 내용을 적어주셔야 합니다")
    private String content;

}
