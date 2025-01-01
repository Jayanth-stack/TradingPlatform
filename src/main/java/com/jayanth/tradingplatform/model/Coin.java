package com.jayanth.tradingplatform.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

@Entity
@Data
public class Coin {
    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("name")
    private String name;

    @JsonProperty("image")
    private String image;

    @JsonProperty("current_price")
    @Column(name = "current_price", precision = 20, scale = 2)
    private BigDecimal currentPrice;

    @JsonProperty("market_cap")
    @Column(name = "market_cap")
    private Long marketCap;

    @JsonProperty("market_cap_rank")
    @Column(name = "market_cap_rank")
    private Integer marketCapRank;

    @JsonProperty("fully_diluted_valuation")
    @Column(name = "fully_diluted_valuation")
    private Long fullyDilutedValuation;

    @JsonProperty("total_volume")
    @Column(name = "total_volume")
    private Long totalVolume;

    @JsonProperty("high_24h")
    @Column(name = "high_24h", precision = 20, scale = 2)
    private BigDecimal high24h;

    @JsonProperty("low_24h")
    @Column(name = "low_24h", precision = 20, scale = 2)
    private BigDecimal low24h;

    @JsonProperty("price_change_24h")
    @Column(name = "price_change_24h", precision = 20, scale = 2)
    private BigDecimal priceChange24h;

    @JsonProperty("price_change_percentage_24h")
    @Column(name = "price_change_percentage_24h", precision = 10, scale = 5)
    private BigDecimal priceChangePercentage24h;

    @JsonProperty("market_cap_change_24h")
    @Column(name = "market_cap_change_24h")
    private Long marketCapChange24h;

    @JsonProperty("market_cap_change_percentage_24h")
    @Column(name = "market_cap_change_percentage_24h", precision = 10, scale = 5)
    private BigDecimal marketCapChangePercentage24h;

    @JsonProperty("circulating_supply")
    @Column(name = "circulating_supply")
    private Long circulatingSupply;

    @JsonProperty("total_supply")
    @Column(name = "total_supply")
    private Long totalSupply;

    @JsonProperty("max_supply")
    @Column(name = "max_supply")
    private Long maxSupply;

    @JsonProperty("ath")
    @Column(name = "ath", precision = 20, scale = 2)
    private BigDecimal ath;

    @JsonProperty("ath_change_percentage")
    @Column(name = "ath_change_percentage", precision = 10, scale = 5)
    private BigDecimal athChangePercentage;

    @JsonProperty("ath_date")
    @Column(name = "ath_date")
    private Instant athDate;

    @JsonProperty("atl")
    @Column(name = "atl", precision = 20, scale = 2)
    private BigDecimal atl;

    @JsonProperty("atl_change_percentage")
    @Column(name = "atl_change_percentage", precision = 10, scale = 5)
    private BigDecimal atlChangePercentage;

    @JsonProperty("atl_date")
    @Column(name = "atl_date")
    private Instant atlDate;

    @JsonProperty("last_updated")
    @Column(name = "last_updated")
    private Instant lastUpdated;
}