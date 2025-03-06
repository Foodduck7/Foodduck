/*
    Title: ReviewService.java
    Author: yjn33
    Date: 2025/02/28
    GitHub: https://github.com/yjn33

    리뷰 구현을 위한 Review Service 입니다.
*/


package com.example.foodduck.review.service;


import com.example.foodduck.order.entity.Order;
import com.example.foodduck.order.repository.OrderRepository;
import com.example.foodduck.review.dto.request.ReviewRequest;
import com.example.foodduck.review.dto.response.ReviewResponse;
import com.example.foodduck.review.entity.Review;
import com.example.foodduck.review.repository.ReviewRepository;
import com.example.foodduck.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    /* // 단건 조회 기능 불가
    public ReviewResponse findReviewById(Long id){
        Review findReview = reviewRepository.findReviewById(id).orElseThrow(
                () -> new EntityNotFoundException("해당"+ id +"값의 리뷰를 찾지 못하였습니다")
        );

        return new ReviewResponse(findReview);
    }
    */


    /*
        리뷰 등록 메서드
    */
    @Transactional
    public ReviewResponse saveReview(Long orderId, ReviewRequest reviewRequest){

        Order order = orderRepository.findOrderById(orderId);
        Review review = new Review(reviewRequest.getRating(), reviewRequest.getContent(), order);
        reviewRepository.save(review);

        return new ReviewResponse(review);
    }


    /*
        리뷰 조회 매서드
    */
    @Transactional
    public Page<ReviewResponse> findAllReviewByStore(Long storeId, int page, int size){
        Pageable pageable = PageRequest.of( (page > 0) ? page - 1 : 0, size, Sort.by("updatedAt").descending());
        Page<Review> reviewPage = reviewRepository.findAllReviewByStore(storeId, pageable);


        return reviewPage.map(ReviewResponse::new);
    }

}
