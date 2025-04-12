package com.jayanth.tradingplatform.config;

import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private CoinRepository coinRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if the database is already seeded
        if (coinRepository.count() == 0) {
            // Create some sample coins
            Coin bitcoin = new Coin();
            bitcoin.setId("bitcoin");
            bitcoin.setSymbol("btc");
            bitcoin.setName("Bitcoin");
            bitcoin.setCurrentPrice(new BigDecimal("30000.00"));
            bitcoin.setMarketCap(1000000000000L);
            bitcoin.setPriceChange24h(new BigDecimal("1000.00"));
            bitcoin.setHigh24h(new BigDecimal("31000.00"));
            bitcoin.setLow24h(new BigDecimal("29000.00"));
            bitcoin.setLastUpdated(Instant.now());

            Coin ethereum = new Coin();
            ethereum.setId("ethereum");
            ethereum.setSymbol("eth");
            ethereum.setName("Ethereum");
            ethereum.setCurrentPrice(new BigDecimal("2000.00"));
            ethereum.setMarketCap(200000000000L);
            ethereum.setPriceChange24h(new BigDecimal("100.00"));
            ethereum.setHigh24h(new BigDecimal("2100.00"));
            ethereum.setLow24h(new BigDecimal("1900.00"));
            ethereum.setLastUpdated(Instant.now());

            // Save the coins to the database
            coinRepository.saveAll(List.of(bitcoin, ethereum));

            System.out.println("Database seeded with sample coins.");
        }
    }
}
