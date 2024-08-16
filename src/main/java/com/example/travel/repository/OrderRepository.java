package com.example.travel.repository;

import com.example.travel.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Page<Order>> findAllByUserUserId(long userId, Pageable pageable);
    Optional<List<Order>> findAllByUserUserId(long userId);
    Optional<List<Order>> findAllByProductProductIdInOrderByOrderDateDesc(List<Long> productId);
    Optional<List<Order>> findAllByUserUserIdAndOrderEndDateIsBeforeAndOrderStatus(long userId, LocalDateTime now, String orderStatus);
    Optional<List<Order>> findAllByUserUserIdAndOrderEndDateIsBeforeAndOrderEndDateIsAfterAndOrderStatus(long userId, LocalDateTime now, LocalDateTime deadLine, String orderStatus);

}
