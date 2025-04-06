package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.model.Wallet;
import com.jayanth.tradingplatform.model.Order;

public interface WalletService {

    Wallet getUserWallet(User user);

    Wallet addBalance(Wallet wallet, long money);

    Wallet findById(long id) throws Exception;

    Wallet walletoWalletTransaction(User sender, Wallet recipient, long amount) throws Exception;

    Wallet payorderPayment(Order order, User user) throws Exception;
}
