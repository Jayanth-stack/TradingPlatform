package com.jayanth.tradingplatform.model;

import com.jayanth.tradingplatform.domain.WithdrawlStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Withdrawl {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    private WithdrawlStatus status;

    private Long amount;

    @ManyToOne
    private User user;

    private LocalDateTime date= LocalDateTime.now();
}
