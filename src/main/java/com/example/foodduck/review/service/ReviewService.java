/*
    Title: ReviewService.java
    Author: yjn33
    Date: 2025/02/28
    GitHub: https://github.com/yjn33

    리뷰 구현을 위한 Review Service 입니다.
*/


package com.example.foodduck.review.service;


import com.example.foodduck.review.dto.response.ReviewResponse;
import com.example.foodduck.review.entity.Review;
import com.example.foodduck.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;


    public ReviewResponse findReviewById(Long id){
        Review findReview = reviewRepository.findReviewById(id).orElseThrow(
                () -> new EntityNotFoundException("해당"+ id +"값의 리뷰를 찾지 못하였습니다")
        );

        return new ReviewResponse(findReview);
    }




}
