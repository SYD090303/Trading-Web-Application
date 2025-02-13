package com.project.TradingWebApp.service;

import com.project.TradingWebApp.entity.TwoFactorOtp;
import com.project.TradingWebApp.entity.UserEntity;

import java.util.Optional;

public interface TwoFactorOtpService {
    TwoFactorOtp generateTwoFactorOtp(UserEntity user, String otp, String token);

    TwoFactorOtp findByUserId(Long userId);

    Optional<TwoFactorOtp> findById(String id);

    boolean verifyTwoFactorOtp(Optional<TwoFactorOtp> twoFactorOtp, String otp);

    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}
