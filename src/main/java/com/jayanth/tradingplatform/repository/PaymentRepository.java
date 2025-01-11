package com.jayanth.tradingplatform.repository;

import com.jayanth.tradingplatform.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentOrder, Long> {
}
