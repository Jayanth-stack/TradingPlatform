package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.PaymentMethod;
import com.jayanth.tradingplatform.domain.PaymentOrderStatus;
import com.jayanth.tradingplatform.model.PaymentOrder;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.PaymentRepository;
import com.jayanth.tradingplatform.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
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
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){
            if(paymentOrder.getMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpayClient = new RazorpayClient(apikey, apiSecretKey);
                Payment payment = razorpayClient.payments.fetch(paymentId);
                Integer amount = payment.get("amount");

                String status = payment.get("status");
                if(status.equals("captured")){
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentRepository.save(paymentOrder);
            return true;
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorPaymentLink(User user, Long amount) throws RazorpayException {

        Long Amount = amount * 100;
        try{
            RazorpayClient razorpayClient = new RazorpayClient(apikey, apiSecretKey);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("name", user.getEmail());

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("reminder_enable", true);

            paymentLinkRequest.put("callback_url", "http://localhost:5173/wallet");
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink payment  = razorpayClient.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentResponse res = new PaymentResponse();
            res.setPaymentURL(paymentLinkUrl);


        } catch (RazorpayException e) {
            System.out.println("Error creating payment link" +e.getMessage());
            throw new RazorpayException(e);
        }

        return null;
    }

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder().addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet?order_id=" + orderId)
                .setCancelUrl("http://localhost:5173/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount * 100)
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Top up Wallet")
                                        .build()).build()).build()).build();

        Session session = Session.create(params);

        System.out.println("Session ________" + session);

        PaymentResponse res = new PaymentResponse();
        res.setPaymentURL(session.getUrl());
        return res;
    }
}
