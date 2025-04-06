package com.jayanth.tradingplatform.controller;

import com.amazonaws.Response;
import com.jayanth.tradingplatform.domain.OrderType;
import com.jayanth.tradingplatform.model.Coin;
import com.jayanth.tradingplatform.model.Order;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.request.CreateOrderRequest;
import com.jayanth.tradingplatform.service.CoinService;
import com.jayanth.tradingplatform.service.OrderService;
import com.jayanth.tradingplatform.service.UserService;
import com.jayanth.tradingplatform.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private UserService userService;

//    @Autowired
//    private WalletTransactionService walletTransactionService;


    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt,
                                                 @RequestBody CreateOrderRequest req)
            throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(req.getCoinId());

        Order order = orderService.processOrder(coin, req.getQuantity(),
                req.getOrderType(),user);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwtToken,
                                              @PathVariable Long orderId) throws Exception {
        if (jwtToken == null || jwtToken.isEmpty()) {
            throw new Exception("token missing...");
        }
        User user = userService.findUserProfileByJwt(jwtToken);
        Order order = orderService.getOrderById(orderId);
        if (order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(order);
        } else {
            throw new Exception("order not found");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser (@RequestHeader("Authorization") String jwt,
                                                            @RequestHeader(required = false) OrderType order_type,
                                                            @RequestHeader(required = false) String asset_symbol) throws Exception {
        Long userId = userService.findUserProfileByJwt(jwt).getId();
        List<Order > usedOrder = orderService.getAllOrdersOfUsers(userId, order_type, asset_symbol);
        return ResponseEntity.ok(usedOrder);
    }


}
