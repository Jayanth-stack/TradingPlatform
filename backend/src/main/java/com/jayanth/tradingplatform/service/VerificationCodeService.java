package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.Verificationtype;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.model.VerifcationCode;

public interface VerificationCodeService {

    VerifcationCode sendVerificationCode(User user , Verificationtype verificationType);

    VerifcationCode getVerificationCode(Long id) throws Exception;

    VerifcationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCode(VerifcationCode verificationCode);
}
