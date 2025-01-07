package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.model.Withdrawl;

import java.util.List;

public class WithdrawlServiceImpl implements WithdrawlService {

    @Override
    public Withdrawl requestWithdrawl(Long amount, User user) {
        return null;
    }

    @Override
    public Withdrawl proceedWithWithdrawl(Long withdrawlId, boolean accept) {
        return null;
    }

    @Override
    public List<Withdrawl> getUsersWithdrawlHistory(User user) {
        return List.of();
    }

    @Override
    public List<Withdrawl> getAllWithdrawlRequest() {
        return List.of();
    }
}
