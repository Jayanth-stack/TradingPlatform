package com.jayanth.tradingplatform.model;

import com.jayanth.tradingplatform.domain.PaymentMethod;
import com.jayanth.tradingplatform.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PaymentOrderStatus status;

    private PaymentMethod method;

    @ManyToOne
    private User user;

}
