package com.jayanth.tradingplatform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Asset {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long Id;

    private double quantity;

    private BigDecimal BuyPrice;

    @OneToOne
    private Coin coin;

    @ManyToOne
    private User user;
}
