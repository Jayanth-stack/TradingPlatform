package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinsList(int page);

    String getMarketChart(String coinId, int days);

    String getCoinDetails(String coinId);

    Coin findById(String coinId);

    String searchCoin(String keyword);

    String getTop50CoinByMarketCap();

    String GetTradingCoins();


}
