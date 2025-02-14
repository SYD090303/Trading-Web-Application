package com.project.TradingWebApp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
/**
 * Represents a TwoFactorOtp entity, used for storing One-Time Passwords (OTPs) specifically for two-factor authentication processes.
 * This entity is designed to hold temporary OTPs generated for user verification during login or sensitive actions requiring 2FA.
 * It associates an OTP with a user and a token, ensuring that each OTP is uniquely linked and can be used for verification.
 */
public class TwoFactorOtp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    /**
     * The unique identifier for the TwoFactorOtp entity.
     * This ID is automatically generated, typically as a UUID, and serves as the primary key for this entity.
     * It is used to uniquely identify each OTP record in the database.
     */
    private String Id;

    /**
     * The One-Time Password (OTP) itself.
     * This field stores the randomly generated OTP string that is sent to the user for verification.
     * It is a sensitive value and should be handled securely, typically with encryption at rest and in transit if necessary.
     */
    private String Otp;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne
    /**
     * The UserEntity associated with this OTP.
     * Establishes a one-to-one relationship with the UserEntity, linking the OTP to a specific user.
     * `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` annotation ensures that this field is only used when deserializing (writing to the object),
     * and it is ignored during serialization (when converting the object to JSON). This is useful to prevent sending user details in responses where OTP is involved.
     */
    private UserEntity user;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    /**
     * A token associated with this OTP, potentially for session management or further verification processes.
     * This could be used to correlate the OTP with a specific login session or action.
     * `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` annotation, similar to the 'user' field, restricts this field to be used only during deserialization,
     * preventing it from being included in JSON responses, which is often desirable for security reasons as tokens may contain sensitive session identifiers.
     */
    private String token;
}