package com.example.travel.repository;

import com.example.travel.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Page<Order>> findAllByUserUserId(long userId, Pageable pageable);
    Optional<List<Order>> findAllByProductProductIdIn(List<Long> productId);
}
