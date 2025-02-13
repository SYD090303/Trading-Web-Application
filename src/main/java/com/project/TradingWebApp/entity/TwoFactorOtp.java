package com.project.TradingWebApp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TwoFactorOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String Id;

    private String Otp;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne
    private UserEntity user;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String token;
}

