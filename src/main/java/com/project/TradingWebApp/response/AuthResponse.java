package com.project.TradingWebApp.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * **AuthResponse Model**
 *
 * Represents the response object for authentication-related API calls.
 */
@Data
@NoArgsConstructor  // Default constructor
@AllArgsConstructor // Constructor with all fields
public class AuthResponse {

    private String token;  // JWT Token
    private boolean status;  // Authentication status
    private String message;  // Response message
    private boolean isTwoFactorAuthEnabled;  // Indicates if 2FA is enabled
    private String session = null;  // Session ID
}
