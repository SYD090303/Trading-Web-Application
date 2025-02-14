package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.config.JwtProvider;
import com.project.TradingWebApp.domain.VerificationType;
import com.project.TradingWebApp.entity.TwoFactorAuth;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.repository.UserRepository;
import com.project.TradingWebApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the UserService interface.
 * Provides user-related operations including authentication, user retrieval, and password management.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Finds a user by their email address.
     *
     * @param userEmail The email of the user to search for.
     * @return The UserEntity associated with the given email.
     * @throws Exception If no user is found with the given email.
     */
    @Override
    public UserEntity findUserByEmail(String userEmail) throws Exception {
        UserEntity user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new Exception("User Not Found");
        }
        return user;
    }

    /**
     * Finds a user by their unique user ID.
     *
     * @param userId The ID of the user.
     * @return The UserEntity corresponding to the given user ID.
     * @throws Exception If no user is found with the given ID.
     */
    @Override
    public UserEntity findUserByUserId(long userId) throws Exception {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new Exception("User Not Found");
        }
        return user.get();
    }

    /**
     * Retrieves a user based on the provided JWT token.
     *
     * @param token The JWT token containing user details.
     * @return The UserEntity extracted from the token.
     * @throws Exception If no user is found for the extracted email.
     */
    @Override
    public UserEntity findUserByToken(String token) throws Exception {
        String userEmail = JwtProvider.getEmailFromToken(token);
        UserEntity user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new Exception("User Not Found");
        }
        return user;
    }

    /**
     * Enables two-factor authentication (2FA) for a user.
     *
     * @param verificationType The type of verification (e.g., EMAIL, SMS).
     * @param sendTo The recipient's email or phone number.
     * @param user The user for whom 2FA is being enabled.
     * @return The updated UserEntity with 2FA enabled.
     */
    @Override
    public UserEntity enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, UserEntity user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setVerificationType(verificationType);

        user.setTwoFactorAuth(twoFactorAuth);

        return userRepository.save(user);
    }

    /**
     * Updates a user's password.
     *
     * @param user The user whose password is being updated.
     * @param newPassword The new password to be set.
     * @return The updated UserEntity with the new password.
     */
    @Override
    public UserEntity updatePassword(UserEntity user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
