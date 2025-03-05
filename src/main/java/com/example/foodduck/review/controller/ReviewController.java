/*
    Title: ReviewController.java
    Author: yjn33
    Date: 2025/02/28
    GitHub: https://github.com/yjn33

    Review 컨트롤러 입니다
*/

package com.example.foodduck.review.controller;


import com.example.foodduck.review.dto.request.ReviewRequest;
import com.example.foodduck.review.dto.response.ReviewResponse;
import com.example.foodduck.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("reviews")
public class ReviewController {

    private final ReviewService reviewService;


    /*
        리뷰 등록 메서드
        url명 수정 필요
        Order에서 id를 주입
    */
    @PostMapping("/{orderId}")
    public ResponseEntity<ReviewResponse> saveReview(
            @PathVariable Long orderId,
            @RequestBody ReviewRequest reviewRequest){
        ReviewResponse reviewResponse = reviewService.saveReview(orderId, reviewRequest);

        return new ResponseEntity<>(reviewResponse, HttpStatus.OK); // status 200
    }


    /*
        가게 기준으로
        수정일(최초 생성일) 기준정렬 리뷰 조회 매서드
        페이지네이션 적용
        한페이지에 20개의 리뷰
    */
    @GetMapping("stores/{storeId}")
    public ResponseEntity<Page<ReviewResponse>> findAllReviewByStore(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ){

        Page<ReviewResponse> reviewResponse = reviewService.findAllReviewByStore(storeId, page, size);

        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }



}
