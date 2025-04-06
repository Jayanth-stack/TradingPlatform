package com.jayanth.tradingplatform.request;

import com.jayanth.tradingplatform.domain.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {

    private String coinId;
    private double quantity;
    private OrderType orderType;
}
