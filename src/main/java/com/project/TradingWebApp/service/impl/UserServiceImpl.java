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


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity findUserByEmail(String userEmail) throws Exception {
        UserEntity user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new Exception("User Not Found");
        }
        return user;

    }

    @Override
    public UserEntity findUserByUserId(long userId) throws Exception {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {

            throw new Exception("User Not Found");
        }
        return user.get();
    }

    @Override
    public UserEntity findUserByToken(String token) throws Exception {
        String userEmail = JwtProvider.getEmailFromToken(token);
        UserEntity user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new Exception("User Not Found");
        }
        return user;
    }

    @Override
    public UserEntity enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, UserEntity user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setVerificationType(verificationType);

        user.setTwoFactorAuth(twoFactorAuth);

        return userRepository.save(user);
    }

    @Override
    public UserEntity updatePassword(UserEntity user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
