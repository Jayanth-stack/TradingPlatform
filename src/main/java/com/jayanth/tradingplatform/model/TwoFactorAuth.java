package com.jayanth.tradingplatform.model;

import com.jayanth.tradingplatform.domain.Verificationtype;
import lombok.Data;

@Data
public class TwoFactorAuth {

    private boolean isEnabled = true;
    private Verificationtype sendTo;

}
