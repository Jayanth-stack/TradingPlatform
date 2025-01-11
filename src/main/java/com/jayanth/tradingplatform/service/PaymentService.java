package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.PaymentMethod;
import com.jayanth.tradingplatform.model.PaymentOrder;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.response.PaymentResponse;

public interface PaymentService {

    PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId);

    PaymentResponse createRazorPaymentLink(User user, Long amount);

    PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId);
}