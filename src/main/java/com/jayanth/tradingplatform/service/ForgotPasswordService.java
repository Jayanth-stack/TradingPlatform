package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.Verificationtype;
import com.jayanth.tradingplatform.model.ForgotPasswordToken;
import com.jayanth.tradingplatform.model.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user, String id, String otp, Verificationtype verificationtype, String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);

}
