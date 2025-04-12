package com.jayanth.tradingplatform.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Coin>> getAllCoins() {
        List<Coin> coins = coinService.getAllCoins();
        return new ResponseEntity<>(coins, HttpStatus.OK);
    }

    @GetMapping("/coins")
    ResponseEntity<List<Coin>> getCoinList(@RequestParam("page") int page) throws Exception {
        List<Coin> coins = coinService.getCoinsList(page);

        return new ResponseEntity<>(coins, HttpStatus.ACCEPTED);

    }
    @GetMapping("/{coinId}/chart")
    ResponseEntity<JsonNode> getMarketChart(@PathVariable String coinId,
                                              @RequestParam ("days") int days) throws Exception {
        String response = coinService.getMarketChart(coinId, days);
        JsonNode node = objectMapper.readTree(response);
        return new ResponseEntity<>(node, HttpStatus.ACCEPTED);

    }

    @GetMapping("/search")
    ResponseEntity<JsonNode> searchCoin(@RequestParam ("q") String keyword) throws Exception {
        String response = coinService.searchCoin(keyword);
        JsonNode node = objectMapper.readTree(response);
        return ResponseEntity.ok(node);

    }
    @GetMapping("/top50coin")
    ResponseEntity<JsonNode> getTop50Coin() throws Exception {
        String response = coinService.getTop50CoinByMarketCap();
        JsonNode node = objectMapper.readTree(response);
        return ResponseEntity.ok(node);
    }

    @GetMapping("/trading")
    ResponseEntity <JsonNode> getTradingCoin (@RequestParam ("page") int page) throws Exception{
        String response = coinService.getTradingCoins();
        JsonNode node = objectMapper.readTree(response);
        return ResponseEntity.ok(node);
    }

    @GetMapping("/coinDetails/{coinId}")
    ResponseEntity<JsonNode> coinDetails(@PathVariable String coinId) throws Exception {
        String response = coinService.getCoinDetails(coinId);
        JsonNode node = objectMapper.readTree(response);
        return ResponseEntity.ok(node);
    }




}
