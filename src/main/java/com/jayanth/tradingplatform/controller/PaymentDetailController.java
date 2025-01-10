package com.jayanth.tradingplatform.controller;


import com.jayanth.tradingplatform.model.PaymentDetails;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.service.PaymentDetailsService;
import com.jayanth.tradingplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentDetailController {

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/addPaymentDetails")
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestHeader ("Authorization") String jwt,
                                                            @RequestBody PaymentDetails paymentDetailsRequest) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        PaymentDetails paymentDetails= paymentDetailsService.addPaymentDetails(paymentDetailsRequest.getAccountNumber(),
                paymentDetailsRequest.getAccountName(), paymentDetailsRequest.getIFscCode(),paymentDetailsRequest.getBankName(), user);

        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);

    }

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getUserPaymentDetails(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentDetails paymentDetails = paymentDetailsService.getUserPaymentDetails(user);

        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
    }
    @PostMapping("/deletePayment")
    public ResponseEntity<PaymentDetails> deletePaymentDetails(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        PaymentDetails paymentDetails = paymentDetailsService.getUserPaymentDetails(user);

        return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
    }

}
