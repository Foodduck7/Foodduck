/*
    Title: ReviewRepository.java
    Author: yjn33
    Date: 2025/02/28
    GitHub: https://github.com/yjn33

    리뷰 구현을 위한 Review Repository Interface 입니다.
*/

package com.example.foodduck.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;

import com.example.foodduck.review.entity.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /* //단건 조회 기능 불가
    Optional<Review> findReviewById(Long id);
    */


    /*
        가게 메뉴에 따라 리뷰 조회
    */
    //@Query("SELECT r FROM Review r " + "WHERE r.user.id = :user_id " + "ORDER BY SIZE(r.contentList) DESC")
    //Optional<Review> findAllReview(@Param("userId") Long userId);



    /*
        별점 범위에 따라 조회 BETWEEN 연산자 활용

    @Query("SELECT p FROM Post p " + "WHERE FUNCTION('DATE', p.modifiedAt) BETWEEN :startDate AND :endDate ")
    Optional<Review> findAllReviewByRatingRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,

    );
    */







}
