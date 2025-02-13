package com.project.TradingWebApp.service;

import com.project.TradingWebApp.domain.VerificationType;
import com.project.TradingWebApp.entity.TwoFactorOtp;
import com.project.TradingWebApp.entity.UserEntity;

public interface UserService {

    public UserEntity findUserByEmail(String email) throws Exception;
    public UserEntity findUserByUserId(long userId) throws Exception;
    public UserEntity findUserByToken(String token) throws Exception;
    public UserEntity enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, UserEntity user);
    public UserEntity updatePassword(UserEntity user, String newPassword);
}
