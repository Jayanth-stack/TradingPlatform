package com.jayanth.tradingplatform.repository;

import com.jayanth.tradingplatform.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
        OrderItem findByOrderId(Long orderId);
}
