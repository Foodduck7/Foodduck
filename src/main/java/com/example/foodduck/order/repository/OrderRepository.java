package com.example.foodduck.order.repository;

import com.example.foodduck.exception.ApplicationException;
import com.example.foodduck.order.entity.Order;
import org.apache.coyote.BadRequestException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

/**
 * 주문 기능 쿼리를 DB로 보내 실행하는 인터페이스
 * @author 이호수
 * @version JPA repository 를 상속받아 기본 기능 사용
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    default Order findOrderById(Long id) {
        return findById(id).orElseThrow(() -> new ApplicationException("해당 주문은 존재 안함", HttpStatus.BAD_REQUEST));
    }
}
