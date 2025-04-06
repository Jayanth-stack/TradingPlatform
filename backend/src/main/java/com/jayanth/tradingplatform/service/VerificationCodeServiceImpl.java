package com.jayanth.tradingplatform.service;

import com.jayanth.tradingplatform.domain.Verificationtype;
import com.jayanth.tradingplatform.model.User;
import com.jayanth.tradingplatform.model.VerifcationCode;
import com.jayanth.tradingplatform.repository.VerificationCodeRepository;
import com.jayanth.tradingplatform.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{

    @Autowired
    public VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerifcationCode sendVerificationCode(User user, Verificationtype verificationType) {
        VerifcationCode verificationCodeSaved = new VerifcationCode();
        verificationCodeSaved.setOtp(OtpUtils.generateOtp());
        verificationCodeSaved.setVerificationtype(verificationType);
        verificationCodeSaved.setUser(user);

        return verificationCodeRepository.save(verificationCodeSaved);
    }

    @Override
    public VerifcationCode getVerificationCode(Long id) throws Exception {
        Optional<VerifcationCode> verificationCode = verificationCodeRepository.findById(id);
        if(verificationCode.isPresent()) {
            return verificationCode.get();
        }
        throw new Exception("Verification code not found");
    }

    @Override
    public VerifcationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCode(VerifcationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }

}
