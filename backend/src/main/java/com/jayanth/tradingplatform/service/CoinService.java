package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinsList(int page) throws Exception;

    String getMarketChart(String coinId, int days) throws Exception;

    String getCoinDetails(String coinId) throws Exception;

    Coin findById(String coinId) throws Exception;

    String searchCoin(String keyword) throws Exception;

    String getTop50CoinByMarketCap() throws Exception;

    String getTradingCoins() throws Exception;

    List<Coin> getAllCoins();


}
