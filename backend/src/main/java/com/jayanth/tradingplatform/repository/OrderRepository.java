package com.jayanth.tradingplatform.repository;

import com.jayanth.tradingplatform.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long orderId);
}
