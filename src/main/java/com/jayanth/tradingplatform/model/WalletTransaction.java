package com.jayanth.tradingplatform.model;

import com.jayanth.tradingplatform.domain.WalletTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class WalletTransaction {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Wallet wallet;

    private WalletTransactionType transactionType;

    private LocalDateTime date;

    private String transferId;

    private String purpose;

    private Long amount;

}
