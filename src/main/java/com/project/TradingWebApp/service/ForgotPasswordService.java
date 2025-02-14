package com.project.TradingWebApp.service;

import com.project.TradingWebApp.domain.VerificationType;
import com.project.TradingWebApp.entity.ForgotPassword;
import com.project.TradingWebApp.entity.UserEntity;

/**
 * Service interface for managing forgot password functionality.
 * Handles OTP generation, token creation, and deletion for password reset.
 */
public interface ForgotPasswordService {

    /**
     * Creates a new forgot password token with OTP for a user.
     *
     * @param user             The user requesting a password reset.
     * @param id               Unique identifier for the token.
     * @param otp              The one-time password for verification.
     * @param verificationType The type of verification (e.g., email or SMS).
     * @param sendTo           The recipient's email or phone number.
     * @return The created ForgotPassword entity.
     */
    ForgotPassword createToken(UserEntity user, String id, String otp,
                               VerificationType verificationType, String sendTo);

    /**
     * Finds a ForgotPassword entity by its unique identifier.
     *
     * @param id The unique token ID.
     * @return The ForgotPassword entity if found.
     */
    ForgotPassword findById(String id);

    /**
     * Finds a ForgotPassword entity associated with a given user ID.
     *
     * @param userId The ID of the user.
     * @return The ForgotPassword entity linked to the user.
     */
    ForgotPassword findByUserId(Long userId);

    /**
     * Deletes a forgot password record after password reset or expiration.
     *
     * @param forgotPassword The ForgotPassword entity to be deleted.
     */
    void deletePassword(ForgotPassword forgotPassword);
}
