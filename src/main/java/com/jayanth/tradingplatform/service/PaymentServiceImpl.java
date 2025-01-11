package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.PaymentMethod;
import com.jayanth.tradingplatform.model.PaymentOrder;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.PaymentRepository;
import com.jayanth.tradingplatform.response.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String apikey;

    @Value("${razorpay.api.secret}")
    private String apiSecretKey;

    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setMethod(paymentMethod);

        return paymentRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentRepository.findById(id).orElseThrow(() -> new Exception("payment Order not found"));
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) {
        return null;
    }

    @Override
    public PaymentResponse createRazorPaymentLink(User user, Long amount) {
        return null;
    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) {
        return null;
    }
}
