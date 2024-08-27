package com.example.travel.repository;

import com.example.travel.domain.Order;
import com.example.travel.dto.order.OptionSellDto;
import com.example.travel.dto.order.OrderDateStatistics;
import com.example.travel.dto.order.OrderStatusAndCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Page<Order>> findAllByUserUserId(long userId, Pageable pageable);
    Optional<List<Order>> findAllByUserUserId(long userId);
    Optional<List<Order>> findAllByProductProductIdInOrderByOrderDateDesc(List<Long> productId);
    Optional<List<Order>> findAllByUserUserIdAndOrderEndDateIsBeforeAndOrderStatusOrderByOrderDepartureDateDesc(long userId, LocalDateTime now, String orderStatus);
    Optional<List<Order>> findAllByUserUserIdAndOrderEndDateIsBeforeAndOrderEndDateIsAfterAndOrderStatusOrderByOrderDepartureDate(long userId, LocalDateTime now, LocalDateTime deadLine, String orderStatus);
    Optional<List<Order>> findAllByProductProductIdInAndOrderDateIsAfterAndOrderStatusIn(List<Long> productIdList, LocalDateTime orderDate, List<String> orderStatus);

    @Query(value = "SELECT new com.example.travel.dto.order.OptionSellDto( " +
            "po.productOptionId, " +
            "po.productOptionAgeRange, " +
            "SUM(od.orderDetailTravelerCount)) " +
            "FROM Order o " +
            "Join o.orderDetailList od " +
            "Join od.productOption po " +
            "WHERE o.product.productId = :productId " +
            "AND o.orderDate BETWEEN :startDate AND :endDate " +
            "AND (o.orderStatus = '결제완료' OR (o.orderStatus IN ('입금미확인', '미입금') AND o.orderDepartureDate > :today)) " +
            "GROUP BY po.productOptionId, po.productOptionAgeRange"
    )
    List<OptionSellDto> getOptionSellCount(@Param("productId") long productId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("today") LocalDateTime today);

    @Query(value = "SELECT new com.example.travel.dto.order.OrderStatusAndCountDto( " +
            "o.orderStatus, " +
            "COUNT(*)) " +
            "FROM Order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "AND o.product.productId = :productId " +
            "GROUP BY o.orderStatus " +
            "ORDER BY o.orderStatus "
    )
    List<OrderStatusAndCountDto> getOrderStatusStatistics(@Param("productId") long productId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT new com.example.travel.dto.order.OrderDateStatistics( " +
            "o.orderDepartureDate, " +
            "SUM(od.orderDetailTravelerCount)) " +
            "FROM Order o " +
            "JOIN o.orderDetailList od " +
            "WHERE o.orderDepartureDate BETWEEN :startDate AND :endDate " +
            "AND (o.orderStatus = '결제완료' OR (o.orderStatus IN ('입금미확인', '미입금') AND o.orderDepartureDate > :today)) " +
            "AND o.product.productId = :productId " +
            "GROUP BY o.orderDepartureDate "
    )
    List<OrderDateStatistics> getOrderDateStatistics(@Param("productId") long productId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("today") LocalDateTime today);
}
