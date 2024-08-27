package com.example.travel.repository;

import com.example.travel.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("SELECT SUM(od.orderDetailTravelerCount) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.orderDepartureDate BETWEEN :startDate AND :endDate " +
            "AND o.product.productId = :productId")
    Long findSumOfTravelerCount(@Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate,
                                @Param("productId") long productId);
}
