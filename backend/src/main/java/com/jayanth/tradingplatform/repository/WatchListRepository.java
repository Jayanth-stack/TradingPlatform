package com.jayanth.tradingplatform.repository;

import com.jayanth.tradingplatform.model.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {

    WatchList findByUserId(Long userId);
}
