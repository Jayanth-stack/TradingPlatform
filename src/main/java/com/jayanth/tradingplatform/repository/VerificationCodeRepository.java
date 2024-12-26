package com.jayanth.tradingplatform.repository;

import com.jayanth.tradingplatform.model.VerifcationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerifcationCode, Long> {

    public VerifcationCode findByUserId(Long userId);
}
