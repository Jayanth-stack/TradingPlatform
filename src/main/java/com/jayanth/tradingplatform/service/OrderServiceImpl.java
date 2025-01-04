package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.OrderStatus;
import com.jayanth.tradingplatform.domain.OrderType;
import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.model.Order;
import com.jayanth.tradingplatform.model.OrderItem;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
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
        return null;
    }

    @Override
    public List<Order> getAllOrdersOfUsers(Long userId, OrderType orderType, String assetSymbol) {
        return List.of();
    }

    @Override
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) {
        return null;
    }
}
