package com.project.TradingWebApp.service;

import com.project.TradingWebApp.entity.TwoFactorOtp;
import com.project.TradingWebApp.entity.UserEntity;

import java.util.Optional;

/**
 * Interface for managing Two-Factor Authentication (2FA) OTPs.
 */
public interface TwoFactorOtpService {

    /**
     * Generates a new 2FA OTP for a user and stores it.
     *
     * @param user  The user for whom the OTP is generated.
     * @param otp   The generated OTP value.
     * @param token A unique token (e.g., UUID) associated with the OTP.
     * @return The created TwoFactorOtp object.
     */
    TwoFactorOtp generateTwoFactorOtp(UserEntity user, String otp, String token);

    /**
     * Finds a 2FA OTP by the user's ID.
     *
     * @param userId The ID of the user.
     * @return The TwoFactorOtp object if found, otherwise null.
     */
    TwoFactorOtp findByUserId(Long userId);

    /**
     * Finds a 2FA OTP by its ID (token).
     *
     * @param id The ID (token) of the 2FA OTP.
     * @return An Optional containing the TwoFactorOtp object if found.
     */
    Optional<TwoFactorOtp> findById(String id);

    /**
     * Verifies a 2FA OTP.
     *
     * @param twoFactorOtp An Optional containing the TwoFactorOtp object to verify.
     * @param otp          The OTP value provided by the user.
     * @return True if the OTP is valid and matches the stored OTP, false otherwise.
     */
    boolean verifyTwoFactorOtp(Optional<TwoFactorOtp> twoFactorOtp, String otp);

    /**
     * Deletes a 2FA OTP.
     *
     * @param twoFactorOtp The TwoFactorOtp object to delete.
     */
    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}