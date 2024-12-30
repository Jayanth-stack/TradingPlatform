package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.Verificationtype;
import com.jayanth.tradingplatform.model.ForgotPasswordToken;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordServiceImp implements ForgotPasswordService {

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;


    @Override
    public ForgotPasswordToken createToken(User user, String id, String otp, Verificationtype verificationtype, String sendTo) {
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setUser(user);
        token.setSendTo(sendTo);
        token.setVerificationtype(verificationtype);
        token.setOtp(otp);
        token.setId(id);
        return forgotPasswordRepository.save(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> token = forgotPasswordRepository.findById(id);

        return token.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {

        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordRepository.delete(token);
    }
}
