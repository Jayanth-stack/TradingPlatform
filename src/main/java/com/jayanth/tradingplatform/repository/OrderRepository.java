package com.jayanth.tradingplatform.repository;

import com.jayanth.tradingplatform.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByUserId(Long orderId);
}
