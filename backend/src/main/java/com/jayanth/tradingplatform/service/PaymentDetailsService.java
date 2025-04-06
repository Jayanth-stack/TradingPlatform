package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.PaymentDetails;
import com.jayanth.tradingplatform.model.User;

public interface PaymentDetailsService {

    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountName,
                                            String iFscCode, String bankName, User user);

    public PaymentDetails getUserPaymentDetails(User user);


}
