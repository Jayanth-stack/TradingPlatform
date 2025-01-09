package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.PaymentDetails;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.PaymentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService{

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountName, String iFscCode,
                                            String bankName, User user) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountName(accountName);
        paymentDetails.setIFscCode(iFscCode);
        paymentDetails.setBankName(bankName);
        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {

        return paymentDetailsRepository.findByUserId(user.getId());
    }
}
