package com.example.foodduck.order.repository;

import com.example.foodduck.exception.custom.ApplicationException;
import com.example.foodduck.order.entity.Order;
import org.apache.coyote.BadRequestException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 주문 기능 쿼리를 DB로 보내 실행하는 인터페이스
 * @author 이호수
 * @version JPA repository 를 상속받아 기본 기능 사용
 */

public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order findOrderById(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException("해당 주문은 존재 안함", HttpStatus.BAD_REQUEST));
    }
    /**
     * @author 문성준
     * @version 25.03.06 17:36
     * 특정 상태의 일/월간 주문 개수 조회 및 특정 기간 동안 주문 총액 계산하기 위해서 추가.
     */
    // @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :startDate AND o.orderStatus = :status")
    // 특정 상태(REQUESTED)의 일/월 간 주문 개수 조회 -> 일단 계산해야하니 개수 조회로 코드 수정.
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :startDate AND o.orderStatus = 'REQUESTED'")
    long countByCreatedAtAfter(LocalDateTime startDate);



    //@Query("SELECT SUM(o.quantity * m.price) FROM Order o JOIN o.menu m WHERE o.createdAt >= :startDate")
    /*
    quantity 필드가 없으므로, SUM(o.quantity * m.price)을 사용 못함을 판단.
    따라서, 특정 상태(REQUESTED)의 주문 개수 조회로 수정해야 할 것 같음.
     */
    // 메뉴 가격은 반영 되고, 특정 기간 동안 주문 총액을 계산함.
    @Query("SELECT SUM(m.price) FROM Order o JOIN o.menu m WHERE o.createdAt >= :startDate AND o.orderStatus = 'REQUESTED'")
    Long getToTalSalesSince(@Param("startDate") LocalDateTime startDate);
}
