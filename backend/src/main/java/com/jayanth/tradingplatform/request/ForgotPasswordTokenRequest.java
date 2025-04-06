package com.jayanth.tradingplatform.request;

import com.jayanth.tradingplatform.domain.Verificationtype;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {

    private String sendTo;

    private Verificationtype verificationtype;

}
