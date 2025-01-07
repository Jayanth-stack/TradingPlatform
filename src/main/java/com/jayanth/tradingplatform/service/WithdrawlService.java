package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.model.Withdrawl;

import java.util.List;

public interface WithdrawlService {

    Withdrawl requestWithdrawl(Long amount, User user);

    Withdrawl proceedWithWithdrawl(Long withdrawlId, boolean accept);

    List<Withdrawl> getUsersWithdrawlHistory(User user);

    List<Withdrawl> getAllWithdrawlRequest();
}
