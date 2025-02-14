package com.project.TradingWebApp.entity;

import com.project.TradingWebApp.domain.PaymentMethod;
import com.project.TradingWebApp.domain.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private  Long amount;

    private PaymentOrderStatus status;

    private PaymentMethod paymentMethod;

    @ManyToOne
    private UserEntity user;


}
