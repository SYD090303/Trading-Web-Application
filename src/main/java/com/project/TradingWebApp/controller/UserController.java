package com.project.TradingWebApp.controller;

import com.project.TradingWebApp.Request.ForgotPaswordRequest;
import com.project.TradingWebApp.Request.ResetPasswordRequest;
import com.project.TradingWebApp.Utils.OtpUtils;
import com.project.TradingWebApp.domain.VerificationType;
import com.project.TradingWebApp.entity.ForgotPassword;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.entity.VerificationCode;
import com.project.TradingWebApp.response.ApiResponse;
import com.project.TradingWebApp.response.AuthResponse;
import com.project.TradingWebApp.service.ForgotPasswordService;
import com.project.TradingWebApp.service.UserService;
import com.project.TradingWebApp.service.VerificationCodeService;
import com.project.TradingWebApp.service.impl.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    /**
     * Retrieves the user profile.
     *
     * @param token The authorization token from the request header.
     * @return ResponseEntity containing the UserEntity and HTTP status OK (200) if successful.
     * @throws Exception if user not found or token is invalid.
     */
    @GetMapping("/api/users/profile")
    public ResponseEntity<UserEntity> getUserProfile(@RequestHeader("Authorization") String token) throws Exception {
        UserEntity user = userService.findUserByToken(token);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Sends a verification OTP (One-Time Password) to the user's email based on the verification type.
     * If a verification code already exists for the user, it re-sends the existing code, otherwise, it generates and sends a new one.
     *
     * @param token             The authorization token from the request header to identify the user.
     * @param verificationType  The type of verification requested (e.g., EMAIL). This is specified as a path variable.
     * @return ResponseEntity with a success message and HTTP status OK (200).
     * @throws Exception if user not found or any error during OTP generation or sending.
     */
    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerficationOtp(
            @RequestHeader("Authorization") String token,
            @PathVariable VerificationType verificationType) throws Exception {

        UserEntity user = userService.findUserByToken(token);

        VerificationCode verificationCode = verificationCodeService.findVerificationCodeByUserId(user.getId());

        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }
        if (verificationType == VerificationType.EMAIL) {
            emailServiceImpl.sendVerificationEmail(user.getEmail(), verificationCode.getOtp());
        }

        return new ResponseEntity<>("Verification otp sent successfully", HttpStatus.OK);
    }

    /**
     * Enables two-factor authentication for the user if the provided OTP is valid.
     * It verifies the OTP against the stored verification code for the user and if matched,
     * enables two-factor authentication for the user.
     *
     * @param token The authorization token from the request header to identify the user.
     * @param otp   The OTP (One-Time Password) entered by the user, provided as a path variable.
     * @return ResponseEntity containing the updated UserEntity with two-factor authentication enabled and HTTP status OK (200) if successful.
     * @throws Exception if OTP is incorrect or user not found or any issue during enabling two-factor authentication.
     */
    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<UserEntity> enableTwoFactorAuthentication(
            @RequestHeader("Authorization") String token
            , @PathVariable String otp) throws Exception {
        UserEntity user = userService.findUserByToken(token);

        VerificationCode verificationCode = verificationCodeService.findVerificationCodeByUserId(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)
                ? verificationCode.getEmail() : verificationCode.getMobile(); // Mobile is not used in current implementation

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if (isVerified) {
            UserEntity verifiedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCodeById(verificationCode); // Delete verification code after successful verification
            return new ResponseEntity<>(verifiedUser, HttpStatus.OK);
        }
        throw new Exception("Wrong otp"); // Exception if OTP verification fails
    }

    /**
     * Sends a "Forgot Password" OTP to the user's email for password reset process.
     * Generates an OTP and associates it with a ForgotPassword token, then sends the OTP to the user's email.
     * If a forgot password token already exists for the user, it reuses it, otherwise, creates a new one.
     *
     * @param request The ForgotPaswordRequest object in the request body, containing the user's email (sendTo) and verification type (e.g., EMAIL).
     * @return ResponseEntity containing AuthResponse with a session ID (ForgotPassword token ID) and a success message, and HTTP status OK (200).
     * @throws Exception if user not found or any error during OTP generation or sending.
     */
    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
            @RequestBody ForgotPaswordRequest request) throws Exception {

        UserEntity user = userService.findUserByEmail(request.getSendTo()); // Find user by email from the request
        String otp = OtpUtils.generateOtp(); // Generate new OTP
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString(); // Generate unique ID for ForgotPassword token

        ForgotPassword password = forgotPasswordService.findByUserId(user.getId()); // Check if ForgotPassword token already exists
        if (password == null) {
            password = forgotPasswordService.createToken(user, id, otp, request.getVerificationType(), request.getSendTo()); // Create new ForgotPassword token
        }

        if (request.getVerificationType().equals(VerificationType.EMAIL)) {
            emailServiceImpl.sendVerificationEmail(
                    user.getEmail(),
                    password.getOtp()); // Send OTP to user's email
        }
        AuthResponse response = new AuthResponse();
        response.setSession(password.getId()); // Set session ID as ForgotPassword token ID
        response.setMessage("Password reset otp sent successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Resets the user's password after verifying the OTP provided for the "Forgot Password" process.
     * It verifies the OTP against the stored ForgotPassword token and if matched, updates the user's password.
     *
     * @param token   The authorization token from the request header (currently not used in this implementation, but might be intended for future use/security context).
     * @param id      The ID of the ForgotPassword token, provided as a request parameter, to identify the password reset session.
     * @param request The ResetPasswordRequest object in the request body, containing the OTP and the new password.
     * @return ResponseEntity containing ApiResponse with a success message and HTTP status ACCEPTED (202) if password reset is successful.
     * @throws Exception if OTP is incorrect or ForgotPassword token not found or any issue during password update.
     */
    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestHeader("Authorization") String token, // Authorization token from header (currently not used)
            @RequestParam String id, // ForgotPassword token ID from request parameter
            @RequestBody ResetPasswordRequest request) throws Exception {


        ForgotPassword password = forgotPasswordService.findById(id); // Find ForgotPassword token by ID

        boolean isVerified = password.getOtp().equals(request.getOtp()); // Verify OTP

        if (isVerified) {
            userService.updatePassword(password.getUser(), request.getPassword()); // Update user's password
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Password reset successfully");
            return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED); // Return success response
        }
        throw new Exception("Wrong otp"); // Exception if OTP verification fails
    }
}