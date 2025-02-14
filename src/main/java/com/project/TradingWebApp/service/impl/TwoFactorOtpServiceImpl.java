package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.entity.TwoFactorOtp;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.repository.TwoFactorOtpRepository;
import com.project.TradingWebApp.service.TwoFactorOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the TwoFactorOtpService interface.
 * Handles OTP generation, verification, and deletion for two-factor authentication.
 */
@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService {

    @Autowired
    private TwoFactorOtpRepository twoFactorOtpRepository;

    /**
     * Generates a new two-factor OTP entry for a user.
     *
     * @param user  The user for whom the OTP is generated.
     * @param otp   The OTP value.
     * @param token The authentication token associated with the OTP.
     * @return The saved TwoFactorOtp entity.
     */
    @Override
    public TwoFactorOtp generateTwoFactorOtp(UserEntity user, String otp, String token) {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        TwoFactorOtp twoFactorOtp = new TwoFactorOtp();
        twoFactorOtp.setId(id);
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setToken(token);
        twoFactorOtp.setUser(user);

        return twoFactorOtpRepository.save(twoFactorOtp);
    }

    /**
     * Retrieves a TwoFactorOtp entry by user ID.
     *
     * @param userId The ID of the user.
     * @return The corresponding TwoFactorOtp entity, if found.
     */
    @Override
    public TwoFactorOtp findByUserId(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    /**
     * Retrieves a TwoFactorOtp entry by its unique ID.
     *
     * @param id The unique ID of the OTP entry.
     * @return An Optional containing the TwoFactorOtp entity, if found.
     */
    @Override
    public Optional<TwoFactorOtp> findById(String id) {
        return twoFactorOtpRepository.findById(id);
    }

    /**
     * Verifies whether the provided OTP matches the stored OTP.
     *
     * @param twoFactorOtp An Optional containing the stored TwoFactorOtp entity.
     * @param otp          The OTP entered by the user.
     * @return True if the provided OTP matches the stored OTP, otherwise false.
     */
    @Override
    public boolean verifyTwoFactorOtp(Optional<TwoFactorOtp> twoFactorOtp, String otp) {
        return twoFactorOtp.map(TwoFactorOtp::getOtp)
                .filter(storedOtp -> storedOtp.equals(otp))
                .isPresent();
    }

    /**
     * Deletes a given TwoFactorOtp entry from the database.
     *
     * @param twoFactorOtp The TwoFactorOtp entity to be deleted.
     */
    @Override
    public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp) {
        twoFactorOtpRepository.delete(twoFactorOtp);
    }
}
