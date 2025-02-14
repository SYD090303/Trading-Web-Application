package com.project.TradingWebApp.entity;

import com.project.TradingWebApp.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
/**
 * Represents a VerificationCode entity, used to store verification codes (OTPs) for user verification processes.
 * This entity is designed to handle temporary verification codes generated for various purposes, such as email verification or enabling two-factor authentication.
 * It links a verification code to a specific user and tracks the verification type and delivery method (email or mobile, although mobile is not fully utilized in the current implementation).
 */
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    /**
     * The unique identifier for the verification code record.
     * This ID is automatically generated, typically as a UUID or a database-generated sequence, and serves as the primary key.
     * It is used to uniquely identify each verification code entry in the database.
     */
    private Long id;

    /**
     * The One-Time Password (OTP) for verification.
     * This field stores the randomly generated OTP string.
     * This is a temporary code used to verify the user's identity or contact information for a specific action.
     */
    private String otp;

    @OneToOne
    /**
     * The UserEntity associated with this verification code.
     * Establishes a one-to-one relationship with the UserEntity, linking the verification code to a specific user.
     * This ensures that each verification code is associated with exactly one user in the system.
     */
    private UserEntity user;

    /**
     * The type of verification process for which this code is generated.
     * Uses the `VerificationType` enum to specify the context of verification, such as EMAIL verification or PHONE verification.
     * This helps in distinguishing the purpose of each verification code.
     */
    private VerificationType verificationType;

    /**
     * The email address to which the verification code was sent, if the `verificationType` is EMAIL.
     * This field stores the email address of the user for email-based verification processes.
     * It is used to track where the verification code was sent and to ensure it matches the user's registered email.
     */
    private String email;

    /**
     * The mobile number to which the verification code was sent, if the `verificationType` is MOBILE.
     * This field is intended to store the mobile number for SMS-based verification processes.
     * However, based on the provided code snippets, SMS/mobile verification is not fully implemented or utilized in the application's logic, particularly in OTP sending and verification flows.
     */
    private String mobile;

}