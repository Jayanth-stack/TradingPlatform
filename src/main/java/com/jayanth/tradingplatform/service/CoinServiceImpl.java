package com.jayanth.tradingplatform.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.repository.CoinRepository;
import org.apache.http.impl.bootstrap.HttpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CoinServiceImpl implements CoinService {

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<Coin> getCoinsList(int page) throws Exception {
        String url ="https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity <String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            List<Coin> coinList = objectMapper.readValue(response.getBody(), new TypeReference<List<Coin>>(){});

            return coinList;
        }
        catch(HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url ="https://api.coingecko.com/api/v3/" + coinId +"/markets?vs_currency=usd&=days=" + days;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity <String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();
        }
        catch(HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getCoinDetails(String coinId) throws Exception {
        String url ="https://api.coingecko.com/api/v3/coins/" + coinId;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity <String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            JsonNode node = objectMapper.readTree(response.getBody());
            Coin coin = new Coin();
            coin.setId(node.get("id").asText());
            coin.setName(node.get("name").asText());
            coin.setSymbol(node.get("symbol").asText());
            coin.setImage(node.get("image").get("large").asText());

            JsonNode marketData = node.get("market_data");
            coin.setCurrentPrice(BigDecimal.valueOf(marketData.get("current_price").get("usd").asDouble()));
            coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
            coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
            coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
            coin.setHigh24h(BigDecimal.valueOf(marketData.get("high_24").get("usd").asDouble()));
            coin.setLow24h(BigDecimal.valueOf(marketData.get("low_24").get("usd").asDouble()));
            coin.setPriceChange24h(BigDecimal.valueOf(marketData.get("price_change_24h").get("usd").asDouble()));
            coin.setPriceChangePercentage24h(BigDecimal.valueOf(marketData.get("price_change_24h").get("usd").asDouble()));

            coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
            coin.setMarketCapChangePercentage24h(BigDecimal.valueOf(marketData.get("market_cap_change_percentage_24").asLong()));
            coin.setTotalSupply(marketData.get("total_supply").get("usd").asLong());

            coinRepository.save(coin);

            return response.getBody();
        }
        catch(HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Coin findById(String coinId) throws Exception {
        Optional<Coin> optionalCoin = coinRepository.findById(coinId);
        if(optionalCoin.isEmpty()){
            throw new Exception("coin not found");
        }else {
            return optionalCoin.get();
        }
    }

    @Override
    public String searchCoin(String keyword) throws Exception {
        String url ="https://api.coingecko.com/api/v3/search?query=" + keyword;
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity <String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();
        }
        catch(HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }

    }

    @Override
    public String getTop50CoinByMarketCap() throws Exception {
        String url ="https://api.coingecko.com/api/v3/coins/markets/vs_currency=usd&per_page=50&page=1";
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity <String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();
        }
        catch(HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public String getTradingCoins() throws Exception {
        String url ="https://api.coingecko.com/api/v3/search/trading";
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity <String> entity = new HttpEntity<String>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();
        }
        catch(HttpClientErrorException | HttpServerErrorException e){
            throw new Exception(e.getMessage());
        }
    }
}
