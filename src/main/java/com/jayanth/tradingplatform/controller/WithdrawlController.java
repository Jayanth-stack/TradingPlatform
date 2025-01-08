package com.jayanth.tradingplatform.controller;


import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.model.Wallet;
import com.jayanth.tradingplatform.model.Withdrawl;
import com.jayanth.tradingplatform.service.UserService;
import com.jayanth.tradingplatform.service.WalletService;
import com.jayanth.tradingplatform.service.WithdrawlService;
import lombok.With;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/withdrawl")
public class WithdrawlController {

    @Autowired
    private WithdrawlService withdrawlService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;


    @PostMapping("/{amount}")
    public ResponseEntity<Withdrawl> withdrawlRequest(@PathVariable Long amount,
                                                      @RequestHeader ("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);

        Withdrawl withdrawl = withdrawlService.requestWithdrawl(amount, user);

        walletService.addBalance(userWallet, -withdrawl.getAmount());

        return new ResponseEntity<>(withdrawl, HttpStatus.OK);

    }
}
