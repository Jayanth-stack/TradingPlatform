package com.jayanth.tradingplatform.controller;

import com.jayanth.tradingplatform.model.*;
import com.jayanth.tradingplatform.response.PaymentResponse;
import com.jayanth.tradingplatform.service.OrderService;
import com.jayanth.tradingplatform.service.PaymentService;
import com.jayanth.tradingplatform.service.UserService;
import com.jayanth.tradingplatform.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;


    @GetMapping("/user")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/{walletId}/transfer")

    public ResponseEntity<Wallet> walletowalletTransfer(@RequestHeader ("authorization") String jwt,
                                                         @PathVariable long walletId,
                                                        @RequestBody WalletTransaction req) throws Exception {
        User senderUser = userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = walletService.findById(walletId);
        Wallet wallet = walletService.walletoWalletTransaction(senderUser, receiverWallet, req.getAmount());

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(@RequestHeader ("authorization") String jwt,
                                                        @PathVariable long orderId) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);

        Wallet wallet = walletService.payorderPayment(order, user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/deposit")
    public ResponseEntity<Wallet> addBalanceToWallet(@RequestHeader ("authorization") String jwt,
                                                   @RequestParam (name = "orderId") Long orderId,
                                                   @RequestParam(name = "payment_id") String paymentId) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);

        PaymentOrder paymentOrder = paymentService.getPaymentOrderById(orderId);
        Boolean status = paymentService.proceedPaymentOrder(paymentOrder, paymentId);

        if(status){
            wallet = walletService.addBalance(wallet, paymentOrder.getAmount());
        }


        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }
}
