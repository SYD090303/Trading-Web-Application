package com.project.TradingWebApp.entity;

import com.project.TradingWebApp.domain.VerificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@Embeddable
/**
 * Embeddable entity representing the two-factor authentication (2FA) settings for a user.
 * This class is designed to be embedded within the UserEntity to manage 2FA configurations directly within the user's profile.
 * It includes fields to track if 2FA is enabled and the preferred method of verification.
 */
public class TwoFactorAuth {

    /**
     * Indicates whether two-factor authentication is enabled for the user.
     * Stored as a boolean value, where 'true' signifies that 2FA is active, and 'false' means it is disabled.
     * The `@Column(columnDefinition = "TINYINT(1)")` annotation ensures that boolean values are stored as TINYINT(1) in MySQL,
     * which is an efficient way to represent boolean data in the database.
     */
    @Column(name = "is_enabled", columnDefinition = "TINYINT(1)")
    private boolean isEnabled = false;

    /**
     * Specifies the type of verification method used for two-factor authentication.
     * Uses the `VerificationType` enum to define the method, such as EMAIL or SMS (though SMS is not implemented in the provided code).
     * `@Enumerated(EnumType.STRING)` annotation is crucial for persisting the enum as a String in the database,
     * making it more readable and maintainable compared to storing enum ordinals.
     */
    @Enumerated(EnumType.STRING) // Added to handle enum storage
    private VerificationType verificationType;
}