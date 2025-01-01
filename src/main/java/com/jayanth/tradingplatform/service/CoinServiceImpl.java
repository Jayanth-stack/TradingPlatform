package com.jayanth.tradingplatform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.List;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Coin> getCoinsList(int page) {
        String url ="https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET);
        }
        return List.of();
    }

    @Override
    public String getMarketChart(String coinId, int days) {
        return "";
    }

    @Override
    public String getCoinDetails(String coinId) {
        return "";
    }

    @Override
    public Coin findById(String coinId) {
        return null;
    }

    @Override
    public String searchCoin(String keyword) {
        return "";
    }

    @Override
    public String getTop50CoinByMarketCap() {
        return "";
    }

    @Override
    public String GetTradingCoins() {
        return "";
    }
}
