/*
    Title: ReviewRepository.java
    Author: yjn33
    Date: 2025/02/28
    GitHub: https://github.com/yjn33

    리뷰 구현을 위한 Review Repository Interface 입니다.
*/

package com.example.foodduck.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

import com.example.foodduck.review.entity.Review;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findReviewById(Long id);

}
