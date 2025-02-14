package com.project.TradingWebApp.Utils;

import java.util.Random;

/**
 * Utility class for generating One-Time Passwords (OTPs).
 */
public class OtpUtils {

    /**
     * Generates a random numeric OTP of a specified length (default is 6).
     *
     * @return A string representing the generated OTP.
     */
    public static String generateOtp() {
        int otpLength = 6; // You could make this a parameter if needed
        Random rand = new Random();

        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(rand.nextInt(10)); // Generates a random digit (0-9)
        }
        return otp.toString();
    }


    /**
     * Generates a random numeric OTP of a specified length.
     *
     * @param otpLength the length of the OTP to be generated
     * @return A string representing the generated OTP.
     */
    public static String generateOtp(int otpLength) {
        Random rand = new Random();

        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(rand.nextInt(10)); // Generates a random digit (0-9)
        }
        return otp.toString();
    }
}