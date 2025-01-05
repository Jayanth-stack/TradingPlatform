package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.OrderStatus;
import com.jayanth.tradingplatform.domain.OrderType;
import com.jayanth.tradingplatform.model.*;
import com.jayanth.tradingplatform.repository.OrderItemRepository;
import com.jayanth.tradingplatform.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        BigDecimal price = orderItem.getCoin().getCurrentPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        Order order  = new Order();
        order.setUser(user);
        order.setOrderType(orderType);
        order.setOrderItem(orderItem);
        order.setPrice(price);
        order.setTimestamp(LocalDate.from(LocalDateTime.now()));
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(long orderId) {

        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getAllOrdersOfUsers(Long userId, OrderType orderType, String assetSymbol) {

        return orderRepository.findByUserId(userId);
    }

    private OrderItem createOrderItem(Coin coin, double quantity, BigDecimal buyPrice, double sellPrice){
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
        if(quantity <= 0){
            throw new Exception("quantity should be greater than 0");

        }
        BigDecimal buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);

        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);

        walletService.payorderPayment(order, user);

        order.setStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);

        //create asset
        return savedOrder;
    }

    @Transactional
    public Order SellAsset(Coin coin, double quantity, User user) throws Exception {
        if(quantity <= 0){
            throw new Exception("quantity should be greater than 0");

        }
        BigDecimal SellPrice = coin.getCurrentPrice();
        BigDecimal buyPrice = assetToSell.getPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, 0, SellPrice);

        Order order = createOrder(user, orderItem, OrderType.SELL);
        orderItem.setOrder(order);

        if(assetToSell.getQuantity() >= quantity){
            order.setStatus(OrderStatus.SUCCESS);
            order.setOrderType(OrderType.SELL);
            Order savedOrder = orderRepository.save(order);
            walletService.payorderPayment(order, user);

            Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
            if(updatedAsset.getQuantity()* coin.getCurrentPrice() <=1){
                assetService.deleteAsset(updatedAsset);
            }
            return savedOrder;
        }

        throw new Exception("Insufficient quantity to sell");

        //create asset

    }


    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
        if(orderType == OrderType.BUY) {
            return buyAsset(coin, quantity, user);
        }
        else if(orderType == OrderType.SELL) {
            return SellAsset(coin, quantity, user);
        }

        throw new Exception("invalid order type");
    }
}