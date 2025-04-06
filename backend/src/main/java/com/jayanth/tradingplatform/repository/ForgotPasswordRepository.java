package com.jayanth.tradingplatform.repository;

import com.jayanth.tradingplatform.model.ForgotPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, String> {
   ForgotPasswordToken findByUserId(Long userId);
}
