package com.project.TradingWebApp.Utils;

import java.util.Random;

public class OtpUtils {
    public static String generateOtp(){
        int optLength = 6;
        Random rand = new Random();

        StringBuilder otp = new StringBuilder(optLength);

        for (int i = 0; i < optLength; i++) {
            otp.append(rand.nextInt(10));
        }
        return otp.toString();
    }
}
