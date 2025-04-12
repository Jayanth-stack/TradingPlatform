package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Override
    public List<Coin> getCoinsList(int page) throws Exception {
        return null;
    }

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        return null;
    }

    @Override
    public String getCoinDetails(String coinId) throws Exception {
        return null;
    }

    @Override
    public Coin findById(String coinId) throws Exception {
        return null;
    }

    @Override
    public String searchCoin(String keyword) throws Exception {
        return null;
    }

    @Override
    public String getTop50CoinByMarketCap() throws Exception {
        return null;
    }

    @Override
    public String getTradingCoins() throws Exception {
        return null;
    }

    @Override
    public List<Coin> getAllCoins() {
        return coinRepository.findAll();
    }
}
