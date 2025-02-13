package com.project.TradingWebApp.entity;

import com.project.TradingWebApp.domain.VerificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Embeddable
public class TwoFactorAuth {

    /**
     * Indicates whether two-factor authentication is enabled for the user.
     */
    @Column(name = "is_enabled", columnDefinition = "TINYINT(1)")
    private boolean isEnabled = false;

    /**
     * Specifies the type of verification method used for 2FA.
     */
    @Enumerated(EnumType.STRING) // Added to handle enum storage
    private VerificationType verificationType;
}
