package com.project.TradingWebApp.controller;

import com.project.TradingWebApp.domain.VerificationType;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.service.UserService;
import com.project.TradingWebApp.service.impl.EmailService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/profile")
    public ResponseEntity<UserEntity> getUserProfile(@RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<UserEntity> enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, UserEntity user) {

    }



}
