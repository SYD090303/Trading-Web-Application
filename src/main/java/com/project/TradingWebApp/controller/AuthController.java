package com.project.TradingWebApp.controller;

import com.project.TradingWebApp.Utils.OtpUtils;
import com.project.TradingWebApp.config.JwtProvider;
import com.project.TradingWebApp.entity.TwoFactorOtp;
import com.project.TradingWebApp.entity.UserEntity;
import com.project.TradingWebApp.repository.UserRepository;
import com.project.TradingWebApp.response.AuthResponse;
import com.project.TradingWebApp.service.impl.CustomUserDetailsService;
import com.project.TradingWebApp.service.impl.EmailServiceImpl;
import com.project.TradingWebApp.service.TwoFactorOtpService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * **Authentication Controller**
 * <p>
 * This controller provides authentication-related endpoints, including:
 * - User Registration (`/signup`)
 * - User Login (`/signin`)
 * - Token Refresh (`/refresh-token`)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    /**
     * **User Registration API**
     * <p>
     * Registers a new user and returns a JWT token.
     *
     * @param user The user details provided in the request body.
     * @return AuthResponse containing JWT token and status message.
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody final UserEntity user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(null, false, "User already exists", false, null));
        }

        UserEntity newUser = new UserEntity();
        newUser.setFullName(user.getFullName());
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);

        Authentication auth = authenticate(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String accessToken = JwtProvider.generateAccessToken(auth);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(accessToken, true, "Registration Successful", false, null));
    }

    /**
     * **User Login API**
     * <p>
     * Authenticates the user and returns a JWT token.
     *
     * @param user The user credentials provided in the request body.
     * @return AuthResponse containing JWT token and status message.
     */
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody final UserEntity user) {
        try {
            logger.info("Attempting login for email: " + user.getEmail());
            Authentication auth = authenticate(user.getEmail(), user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(auth);
            String accessToken = JwtProvider.generateAccessToken(auth);

            UserEntity authUser = userRepository.findByEmail(user.getEmail());

            if (user.getTwoFactorAuth().isEnabled()) {
                AuthResponse twoFactorAuth = new AuthResponse();
                twoFactorAuth.setMessage("Two-Factor Authentication Enabled");
                twoFactorAuth.setTwoFactorAuthEnabled(true);
                String otp = OtpUtils.generateOtp();

                TwoFactorOtp oldTwoFactorOtp = twoFactorOtpService.findByUserId(authUser.getId());
                if (oldTwoFactorOtp != null) {
                    twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
                }
                TwoFactorOtp newTwoFactorOtp = twoFactorOtpService.generateTwoFactorOtp(authUser, otp, accessToken);
                emailServiceImpl.sendVerificationEmail(user.getEmail(), otp);
                twoFactorAuth.setSession(newTwoFactorOtp.getId());

                return new ResponseEntity<>(twoFactorAuth, HttpStatus.ACCEPTED);
            }
            return ResponseEntity.ok(new AuthResponse(accessToken, true, "Login Successful", false, "SESSION_ID"));
        } catch (BadCredentialsException e) {
            logger.error("Login failed for email: " + user.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, false, "Invalid email or password", false, null));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOtp(@PathVariable final String otp,
                                                        @RequestParam final String otpId
    ) {
        Optional<TwoFactorOtp> twoFactorOtp = twoFactorOtpService.findById(otpId);

        if (twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp, otp)) {
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two-Factor Authentication Verified");
            authResponse.setTwoFactorAuthEnabled(true);
            authResponse.setSession(twoFactorOtp.get().getToken());
            return new ResponseEntity<>(authResponse, HttpStatus.ACCEPTED);
        }
        throw new BadCredentialsException("Invalid OTP");
    }

    /**
     * **Token Refresh API**
     * <p>
     * Generates a new access token if the provided refresh token is valid.
     *
     * @param refreshToken The refresh token provided in the request header.
     * @return AuthResponse with new access token or an error message.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") final String refreshToken) {
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;

        if (!JwtProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, false, "Invalid or expired refresh token. Please log in again.", false, null));
        }

        String email = JwtProvider.getEmailFromToken(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        String newAccessToken = JwtProvider.generateAccessToken(auth);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, true, "Token Refreshed Successfully", false, "SESSION_ID"));
    }

    /**
     * **Helper Method: Authenticate User**
     * <p>
     * Validates user credentials and returns an authentication object.
     *
     * @param email    The user's email.
     * @param password The user's password.
     * @return Authentication object if credentials are valid.
     * @throws BadCredentialsException If credentials are incorrect.
     */
    private Authentication authenticate(final String email, final String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
