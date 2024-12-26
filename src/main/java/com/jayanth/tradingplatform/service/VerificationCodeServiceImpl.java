package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.VerifcationCode;
import com.jayanth.tradingplatform.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{

    @Autowired
    public VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerifcationCode sendVerificationCode(VerifcationCode verificationCode) {
        return null;
    }

    @Override
    public VerifcationCode getVerificationCode(Long id) {
        return null;
    }

    @Override
    public VerifcationCode getVerificationCodeByUser(Long userId) {
        return null;
    }

    @Override
    public void deleteVerificationCode(VerifcationCode verificationCode) {

    }

}
