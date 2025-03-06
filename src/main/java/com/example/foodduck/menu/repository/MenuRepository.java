package com.example.foodduck.menu.repository;

import com.example.foodduck.menu.entity.Menu;
import com.example.foodduck.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStore(Store store);

    /**
     * @author 문성준
     * @version 25.03.06 16:32
     * 특정 기간 동안 메뉴별 판매량
     */
//    @Query("SELECT m.menuName, COUNT(o) " +
//            "FROM Order o JOIN o.menu m " +
//            "WHERE o.createdAt >= :date " +
//            "GROUP BY m.menuName")

    /*
    코드 수정, order 주문 할 때, 수량을 입력하지 않고 그냥 userId, storeId, menuId만 요청하는 것을 확인.
    따라서,  Order가 REQUESTED 상태인 경우만 판매량에 포함하도록 수정함.
    즉, 주문 건수에 대해서만 계산 가능함. quantity가 없기 때문에.
    */

    // LocalDateTime 사용으로 -> 일부 수정
    @Query("SELECT m.menuName, COUNT(o) " +
                "FROM Order o JOIN o.menu m " +
                "WHERE o.createdAt >= :startDate AND o.orderStatus = 'REQUESTED' " +
                "GROUP BY m.menuName")
    Map<String, Long> getMenuSalesSince(@Param("date") LocalDateTime startDate);
}
