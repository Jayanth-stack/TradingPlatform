package com.jayanth.tradingplatform.repository;

import com.jayanth.tradingplatform.model.Withdrawl;
import lombok.With;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WithdrawlRepository extends JpaRepository<Withdrawl, Long> {
    List<Withdrawl> findByUserId(Long userId);
}
