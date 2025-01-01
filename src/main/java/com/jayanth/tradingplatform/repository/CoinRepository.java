package com.jayanth.tradingplatform.repository;

import com.jayanth.tradingplatform.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Integer> {

}
