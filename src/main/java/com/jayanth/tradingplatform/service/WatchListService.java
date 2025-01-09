package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.model.WatchList;

public interface WatchListService {

    WatchList findUserWatchList(Long userId) throws Exception;

    WatchList createWatchList(User user);

    WatchList findById(Long id) throws Exception;

    Coin addItemToWatchList(Coin coin, User user) throws Exception;
}
