package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.OrderType;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.model.Wallet;
import com.jayanth.tradingplatform.repository.WalletRepository;
import com.jayanth.tradingplatform.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if(wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, long money) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));

        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findById(long id) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if(wallet.isPresent()) {
            return wallet.get();
        }else {
            throw new Exception("wallet not found");
        }
    }

    @Override
    public Wallet walletoWalletTransaction(User sender, Wallet recipient, long amount) throws Exception {
        Wallet senderWallet = getUserWallet(sender);
        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0){
            throw new Exception("insufficient balance");
        }
        BigDecimal senderBalance = senderWallet
                .getBalance()
                .subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);
        BigDecimal recipientBalance = recipient
                .getBalance()
                .add(BigDecimal
                        .valueOf(amount));
        recipient.setBalance(recipientBalance);
        walletRepository.save(recipient);
        return senderWallet;
    }

    @Override
    public Wallet payorderPayment(Order order, User user) throws Exception {
        Wallet wallet = getUserWallet(user);

        if(order.getOrderType().equals(OrderType.BUY)){
            BigDecimal newbalance = wallet.getBalance().subtract(order.getPrice());
            if(newbalance.compareTo(order.getPrice())<0){
                throw new Exception("Insufficient funds for this transaction");

            }
            wallet.setBalance(newbalance);

        }else{
            if(order.getOrderType().equals(OrderType.SELL)){
                BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
                wallet.setBalance(newBalance);
            }
            walletRepository.save(wallet);

        }


        return wallet;
    }
}
