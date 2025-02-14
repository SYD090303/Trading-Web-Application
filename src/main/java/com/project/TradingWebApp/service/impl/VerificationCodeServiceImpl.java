package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.Utils.OtpUtils;
import com.project.TradingWebApp.domain.VerificationType;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.VerificationCode;
import com.project.TradingWebApp.repository.VerificationCodeRepository;
import com.project.TradingWebApp.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the VerificationCodeService interface.
 * Manages the creation, retrieval, and deletion of verification codes.
 */
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    /**
     * Generates and sends a new verification code for the specified user and verification type.
     *
     * @param user The user for whom the verification code is generated.
     * @param verificationType The type of verification (e.g., email, phone).
     * @return The generated VerificationCode entity saved in the database.
     */
    @Override
    public VerificationCode sendVerificationCode(UserEntity user, VerificationType verificationType) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(OtpUtils.generateOtp()); // Generate a new OTP
        verificationCode.setVerificationType(verificationType);
        verificationCode.setUser(user);
        return verificationCodeRepository.save(verificationCode);
    }

    /**
     * Retrieves a verification code by its unique ID.
     *
     * @param id The ID of the verification code.
     * @return The corresponding VerificationCode entity if found.
     * @throws Exception If no verification code is found for the given ID.
     */
    @Override
    public VerificationCode findVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findById(id);
        if (verificationCodeOptional.isPresent()) {
            return verificationCodeOptional.get();
        }
        throw new Exception("Verification code not found");
    }

    /**
     * Retrieves the latest verification code associated with a given user ID.
     *
     * @param userId The user ID to search for.
     * @return The latest VerificationCode entity for the given user.
     */
    @Override
    public VerificationCode findVerificationCodeByUserId(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    /**
     * Deletes a verification code from the database.
     *
     * @param verificationCode The verification code entity to be deleted.
     */
    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }
}
