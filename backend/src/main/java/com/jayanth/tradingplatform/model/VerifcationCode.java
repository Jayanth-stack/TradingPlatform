package com.jayanth.tradingplatform.model;

import com.jayanth.tradingplatform.domain.Verificationtype;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VerifcationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String otp;

    @OneToOne
    private User user;

    private String email;

    private String mobileNumber;

    private Verificationtype verificationtype;

}
