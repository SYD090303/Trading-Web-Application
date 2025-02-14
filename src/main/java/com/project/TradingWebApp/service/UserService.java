package com.project.TradingWebApp.service;

import com.project.TradingWebApp.domain.VerificationType;
import com.project.TradingWebApp.entity.UserEntity;

/**
 * Interface for managing user-related operations.
 */
public interface UserService {

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user.
     * @return The UserEntity object if found.
     * @throws Exception If an error occurs during the retrieval process.  Consider more specific exception types.
     */
    UserEntity findUserByEmail(String email) throws Exception;

    /**
     * Finds a user by their user ID.
     *
     * @param userId The ID of the user.
     * @return The UserEntity object if found.
     * @throws Exception If an error occurs during the retrieval process. Consider more specific exception types.
     */
    UserEntity findUserByUserId(long userId) throws Exception;

    /**
     * Finds a user by their token (e.g., a registration confirmation token or password reset token).
     *
     * @param token The token associated with the user.
     * @return The UserEntity object if found.
     * @throws Exception If an error occurs during the retrieval process. Consider more specific exception types.
     */
    UserEntity findUserByToken(String token) throws Exception;

    /**
     * Enables two-factor authentication for a user.
     *
     * @param verificationType The type of verification (e.g., EMAIL, SMS).
     * @param sendTo           The address to which the verification code/OTP is sent (e.g., email address, phone number).
     * @param user             The UserEntity for whom 2FA is being enabled.
     * @return The updated UserEntity object.
     */
    UserEntity enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, UserEntity user);

    /**
     * Updates a user's password.
     *
     * @param user        The UserEntity whose password needs to be updated.
     * @param newPassword The new password for the user.
     * @return The updated UserEntity object.
     */
    UserEntity updatePassword(UserEntity user, String newPassword);
}