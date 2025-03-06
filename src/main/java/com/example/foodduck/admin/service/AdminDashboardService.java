package com.example.foodduck.admin.service;

import com.example.foodduck.menu.repository.MenuRepository;
import com.example.foodduck.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 일간/월간 판매된 판매된 메뉴의 단위의 수를 확인하고, 추후에 쿠폰 발급이나, 할인에 쓸거임.
 */
@Service
@RequiredArgsConstructor
public class AdminDashboardService {
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    // private final ReviewRepository reviewRepository; -> 리뷰 완성되면 바로 추ㅏㄱ

    // 통계 : Statistics
    public Map<String, Object> getOrderStatistics(String period) {
        LocalDateTime startDate = calculateStartDate(period);
        long orderCount = orderRepository.countByCreatedAtAfter(startDate);

        Map<String, Object> response = new HashMap<>();
        response.put("period", period);
        response.put("orderCount", orderCount);
        return response;
    }

    // 일/월 간 통계를 구하기 위해 날짜 기준을 반환함.
    private LocalDateTime calculateStartDate(String period) {
        if ("month".equalsIgnoreCase(period)) {
            return LocalDateTime.of(LocalDate.now().minusMonths(1), LocalTime.MIN); //minusDays(1);일/ 월간 구분 되는 로직 생각하면서 짜야함.
        }
        return LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
    }

    // 일/월 간 매출 통계 조회
    public Map<String, Object> getSalesStatistics(String period) {
        LocalDateTime startDate = calculateStartDate(period);
        Long totalSales = orderRepository.getToTalSalesSince(startDate);

        Map<String, Object> response = new HashMap<>();
        response.put("period", period);
        // edit: 25.03.06 18:13 ㅋㅋㅋ 바보같이.. 왜 빨간줄뜨지 하면서 멍하니 있다ㅋㅋㅋ Long으로 선언하여 null을 체크할 수 있도록 수정.
        response.put("totalSales", (totalSales != null) ? totalSales : 0L); // null 방지해서 0L 사용
        return response;
    }

    // 일/월 간 메뉴별 판매량
    public Map<String, Object> getMenuSalesStatistics(String period) {
        LocalDateTime startDate = calculateStartDate(period);
        Map<String, Long> menuSales = menuRepository.getMenuSalesSince(startDate);

        Map<String, Object> response = new HashMap<>();
        response.put("period", period);
        response.put("menuSales", menuSales != null ? menuSales : new HashMap<>()); // 원래는 menu sales 만 넣었는데, 왼쪽으로 수정. null 방지하여 빈 map 반환할 수 있또록.
        return response;
    }

//    // 악성 리뷰 제거 기능 -> 리뷰 리포지토리 완성되면 추가 할 예정
//    public void deleteReview(Long reviewId) {
//        reviewRepository.deleteById(reviewId);
//    }

}
