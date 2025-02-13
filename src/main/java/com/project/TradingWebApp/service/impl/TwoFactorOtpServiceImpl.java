package com.project.TradingWebApp.service.impl;

import com.project.TradingWebApp.entity.TwoFactorOtp;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.repository.TwoFactorOtpRepository;
import com.project.TradingWebApp.service.TwoFactorOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService {

    @Autowired
    TwoFactorOtpRepository twoFactorOtpRepository;
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

    @Override
    public TwoFactorOtp findByUserId(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public Optional<TwoFactorOtp> findById(String id) {
        Optional<TwoFactorOtp> twoFactorOtp = twoFactorOtpRepository.findById(id);
        return twoFactorOtp;
    }

    @Override
    public boolean verifyTwoFactorOtp(Optional<TwoFactorOtp> twoFactorOtp, String otp) {
        return twoFactorOtp.map(TwoFactorOtp::getOtp)
                .filter(storedOtp -> storedOtp.equals(otp))
                .isPresent();
    }


    @Override
    public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp) {
        twoFactorOtpRepository.delete(twoFactorOtp);
    }
}
