package com.example.foodduck.admin.controller;

import com.example.foodduck.admin.service.AdminDashboardService;
import com.example.foodduck.admin.service.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

/**
 * 관리자 통계 api
 * @author 문성준
 * @version 25.03.06.13:51
 * 추가적으로, 메뉴 판매량 통계 구현 (쿠폰 및 day 할인을 위함)
 */
@RestController
@RequestMapping("/admin") // /admin/dashboard 고민중.
@RequiredArgsConstructor
// 공부해보기, @PreAuthorize("hasRole('ROLE_ADMIN')") : https://velog.io/@kwj1830/codestates29, https://au1802.tistory.com/74, https://chae528.tistory.com/75
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;
    private final AdminReviewService adminReviewService;

    @GetMapping("/dashboard/orders")
    public Map<String, Object> getOrderStatistics(@RequestParam String period) {
        return adminDashboardService.getOrderStatistics(period);
    }

    @GetMapping("/dashboard/sales")
    public Map<String, Object> getSalesStatistics(@RequestParam String period) {
        return adminDashboardService.getSalesStatistics(period);
    }

    @GetMapping("/dashboard/menus")
    public Map<String, Object> getMenuSalesStatistics(@RequestParam String period) {
        return adminDashboardService.getMenuSalesStatistics(period);
    }
      // 리뷰 삭제
//    @DeleteMapping("/reviews/{reviewId}")
//    public void deleteReview(@PathVariable Long reviewId) {
//        adminReviewService.deleteReview(reviewId);
//    }

}
