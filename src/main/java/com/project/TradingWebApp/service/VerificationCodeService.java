package com.project.TradingWebApp.service;

import com.project.TradingWebApp.domain.VerificationType;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.VerificationCode;

/**
 * Interface for managing verification codes.
 */
public interface VerificationCodeService {

    /**
     * Sends a verification code to the user.
     *
     * @param user             The user to whom the verification code is sent.
     * @param verificationType The type of verification (e.g., EMAIL, SMS).
     * @return The generated VerificationCode object.
     */
    VerificationCode sendVerificationCode(UserEntity user, VerificationType verificationType);

    /**
     * Finds a verification code by its ID.
     *
     * @param Id The ID of the verification code.
     * @return The VerificationCode object if found.
     * @throws Exception If an error occurs during the retrieval process. Consider more specific exception types.
     */
    VerificationCode findVerificationCodeById(Long Id) throws Exception;

    /**
     * Finds a verification code by the user's ID.
     *
     * @param userId The ID of the user.
     * @return The VerificationCode object if found, otherwise null.
     */
    VerificationCode findVerificationCodeByUserId(Long userId);

    /**
     * Deletes a verification code.
     *
     * @param verificationCode The VerificationCode object to delete.
     */
    void deleteVerificationCodeById(VerificationCode verificationCode);
}