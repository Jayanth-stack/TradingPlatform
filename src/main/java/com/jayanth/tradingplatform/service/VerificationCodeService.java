package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.model.VerifcationCode;

public interface VerificationCodeService {

    VerifcationCode sendVerificationCode(VerifcationCode verificationCode);

    VerifcationCode getVerificationCode(Long id);

    VerifcationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCode(VerifcationCode verificationCode);
}
