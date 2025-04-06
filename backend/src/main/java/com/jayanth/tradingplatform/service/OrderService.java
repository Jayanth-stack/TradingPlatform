package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.OrderType;
import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.model.Order;
import com.jayanth.tradingplatform.model.OrderItem;
import com.jayanth.tradingplatform.model.User;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(long orderId);

    List<Order> getAllOrdersOfUsers(Long userId, OrderType orderType, String assetSymbol);

    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;
}
