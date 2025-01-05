package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.OrderStatus;
import com.jayanth.tradingplatform.domain.OrderType;
import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.model.Order;
import com.jayanth.tradingplatform.model.OrderItem;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.OrderItemRepository;
import com.jayanth.tradingplatform.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private OrderItem createOrderItem(Coin coin, double quantity, double buyPrice, double sellPrice){
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem);
    }

    public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
        if(quantity <= 0){
            throw new Exception("quantity should be greater than 0");

        }
        BigDecimal buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);

        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);

        walletService.payorderPayment(order, user);


        return null;
    }

    @Override
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) {


        return null;
    }
}
