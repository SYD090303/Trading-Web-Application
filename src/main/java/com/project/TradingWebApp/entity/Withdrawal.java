package com.project.TradingWebApp.entity;

import com.project.TradingWebApp.domain.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private WithdrawalStatus withdrawalStatus;


    private Long amount;
    @ManyToOne
    private UserEntity user;

    private LocalDateTime date = LocalDateTime.now();


}
