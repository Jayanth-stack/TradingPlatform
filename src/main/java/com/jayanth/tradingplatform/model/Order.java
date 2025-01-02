package com.jayanth.tradingplatform.model;

import com.jayanth.tradingplatform.domain.OrderStatus;
import com.jayanth.tradingplatform.domain.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Order {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private OrderType orderType;

    @Column(nullable = false)
    private BigDecimal price;

    private LocalDate timestamp= LocalDate.now();

    @Column(nullable = false)
    private OrderStatus status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderItem orderItem;


}
