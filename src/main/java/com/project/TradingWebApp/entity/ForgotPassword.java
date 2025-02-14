package com.project.TradingWebApp.entity;

import com.project.TradingWebApp.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
/**
 * Represents a ForgotPassword entity, used to manage the password reset process for users.
 * This entity stores temporary information required to verify a user's identity when they request to reset their password.
 * It links a user to a unique token (ID), an OTP (One-Time Password), and specifies the verification channel.
 */
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    /**
     * The unique identifier for the ForgotPassword request.
     * This ID is automatically generated, typically as a UUID, and is used to track and retrieve the password reset request.
     * It acts as the primary key for this entity.
     */
    private String id;

    @OneToOne
    /**
     * The UserEntity associated with this ForgotPassword request.
     * This establishes a one-to-one relationship with the UserEntity, linking the password reset process to a specific user.
     * It allows the system to identify which user is requesting a password reset.
     */
    private UserEntity user;

    /**
     * The One-Time Password (OTP) generated for the password reset verification.
     * This OTP is sent to the user via the specified verification channel (e.g., email).
     * The user must provide this OTP to verify their identity and proceed with the password reset.
     * This is a temporary password used only for the password reset process and should have a limited lifespan.
     */
    private String otp;

    /**
     * The VerificationType indicating the method used to send the OTP.
     * This specifies whether the OTP was sent via EMAIL or potentially other methods like SMS (though SMS is not explicitly used in the provided code).
     * It determines how the user will receive the OTP for verification.
     */
    private VerificationType verificationType;

    /**
     * The destination to which the OTP was sent for verification.
     * This field stores the email address or mobile number (depending on VerificationType) where the OTP was sent.
     * It's used to ensure the OTP is delivered to the correct user's contact information.
     */
    private String sendTo;

}