package com.jayanth.tradingplatform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double quantity;

    @ManyToOne
    private Coin coin;

    private BigDecimal buyPrice;

    private BigDecimal sellPrice;

    @JsonIgnore
    @OneToOne
    private Order order;
}
