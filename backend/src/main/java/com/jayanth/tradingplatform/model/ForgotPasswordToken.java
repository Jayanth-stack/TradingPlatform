package com.jayanth.tradingplatform.model;

import com.jayanth.tradingplatform.domain.Verificationtype;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgotPasswordToken {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String Id;

    @OneToOne
    private User user;

    private String otp;

    private Verificationtype verificationtype;

    private String sendTo;


}
