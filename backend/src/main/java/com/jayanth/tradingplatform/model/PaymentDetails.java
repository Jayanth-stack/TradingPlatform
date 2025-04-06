package com.jayanth.tradingplatform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaymentDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    private String accountNumber;

    private String accountName;

    private String iFscCode;

    private String bankName;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;


}
