package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.domain.VerificationType;
import com.project.TradingWebApp.entity.ForgotPassword;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.repository.ForgotPasswordRepository;
import com.project.TradingWebApp.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the ForgotPasswordService interface.
 * This class provides methods for creating, finding, and deleting forgot password tokens.
 */
@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    /**
     * Creates a new forgot password token and saves it to the database.
     *
     * @param user             The user for whom the token is generated.
     * @param id               The unique identifier for the token (e.g., UUID).
     * @param otp              The one-time password (OTP) associated with the token.
     * @param verificationType The type of verification (e.g., EMAIL, SMS).
     * @param sendTo           The address to which the OTP is sent (e.g., email address, phone number).
     * @return The created ForgotPassword object.
     */
    @Override
    public ForgotPassword createToken(UserEntity user,
                                      String id,
                                      String otp,
                                      VerificationType verificationType,
                                      String sendTo) {
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setUser(user);
        forgotPassword.setId(id);
        forgotPassword.setSendTo(sendTo);
        forgotPassword.setVerificationType(verificationType);
        forgotPassword.setOtp(otp);
        return forgotPasswordRepository.save(forgotPassword);
    }

    /**
     * Finds a forgot password token by its ID.
     *
     * @param id The ID of the forgot password token.
     * @return The ForgotPassword object if found, otherwise null.
     */
    @Override
    public ForgotPassword findById(String id) {
        Optional<ForgotPassword> forgotPassword = forgotPasswordRepository.findById(id);

        return forgotPassword.orElse(null);
    }

    /**
     * Finds a forgot password token by the user's ID.
     *
     * @param userId The ID of the user.
     * @return The ForgotPassword object if found, otherwise null.
     */
    @Override
    public ForgotPassword findByUserId(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    /**
     * Deletes a forgot password token from the database.
     *
     * @param forgotPassword The ForgotPassword object to be deleted.
     */
    @Override
    public void deletePassword(ForgotPassword forgotPassword) {
        forgotPasswordRepository.delete(forgotPassword);
    }
}