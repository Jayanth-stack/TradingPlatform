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

import java.util.List;

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
    public ResponseEntity<?> withdrawlRequest(@PathVariable Long amount,
                                                      @RequestHeader ("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);

        Withdrawl withdrawl = withdrawlService.requestWithdrawl(amount, user);

        walletService.addBalance(userWallet, -withdrawl.getAmount());

        return new ResponseEntity<>(withdrawl, HttpStatus.OK);

    }
    @PatchMapping("/admin/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawl(@PathVariable Long id, @PathVariable boolean accept,
                                             @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Withdrawl withdrawl = withdrawlService.proceedWithWithdrawl(id, accept);

        Wallet userWallet = walletService.getUserWallet(user);
        if(!accept){
            walletService.addBalance(userWallet, withdrawl.getAmount());
        }

        return new ResponseEntity<>(withdrawl, HttpStatus.OK);

    }

    @GetMapping("/api/withdrawl")
    public ResponseEntity<List<Withdrawl>> getWithdrawlHistory(@RequestHeader ("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        List<Withdrawl> withdrawl = withdrawlService.getUsersWithdrawlHistory(user);

        return new ResponseEntity<>(withdrawl, HttpStatus.OK);

    }

    @GetMapping("/admin")
    public ResponseEntity<List<Withdrawl>> getAllWithdrawlRequest(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        List<Withdrawl> withdrawl = withdrawlService.getAllWithdrawlRequest();

        return new ResponseEntity<>(withdrawl, HttpStatus.OK);
    }


}
